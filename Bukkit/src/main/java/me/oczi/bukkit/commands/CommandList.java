package me.oczi.bukkit.commands;

import com.google.common.collect.Lists;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.OptArg;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import me.oczi.bukkit.internal.MemoryManager;
import me.oczi.bukkit.internal.commandflow.CommandFlow;
import me.oczi.bukkit.objects.Gender;
import me.oczi.bukkit.objects.Home;
import me.oczi.bukkit.objects.Proposal;
import me.oczi.bukkit.objects.collections.HomeList;
import me.oczi.bukkit.objects.collections.PartnershipTop;
import me.oczi.bukkit.objects.partnership.Partnership;
import me.oczi.bukkit.objects.player.MargaretPlayer;
import me.oczi.bukkit.objects.player.PlayerData;
import me.oczi.bukkit.objects.player.PlayerDataPair;
import me.oczi.bukkit.storage.yaml.MargaretYamlStorage;
import me.oczi.bukkit.utils.*;
import me.oczi.bukkit.utils.settings.EnumSetting;
import me.oczi.bukkit.utils.settings.PlayerSettings;
import me.oczi.common.utils.CommonsUtils;
import net.kyori.text.TextComponent;
import net.kyori.text.event.ClickEvent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static me.oczi.bukkit.utils.CommandPreconditions.checkInstanceOfPlayer;

@Command(
    names = {"list", "ls"},
    desc = "%translatable:list.desc%",
    permission = "margaret.list")
public class CommandList implements CommandClass {

  @Command(
      names = {"help", "?"},
      desc = "%translatable:list.help.desc%")
  public void mainCommand(CommandSender sender,
                          CommandFlow commandFlow,
                          @OptArg("") String arg) {
    Commands.composeFullChildrenHelp(sender,
        commandFlow.getSubCommandsOf("list"),
        "margaret",
        "list");
  }

  @Command(
      names = {"top-partner", "partners", "partner"},
      desc = "%translatable:list.top.partner.desc%")
  public void partners(CommandSender sender,
                       MemoryManager memoryManager,
                       @OptArg("") String numPage) {
    PartnershipTop partnershipTop = memoryManager.getPartnerTop();
    int page;
    if (!CommonsUtils.isNullOrEmpty(numPage)) {
      page = CommonsUtils.parseInt(numPage);
      if (page <= 0) {
        page = 1;
      }
    } else {
      page = 1;
    }
    MessageUtils.compose(sender,
        Messages.LIST_PARTNER_HEADER,
        true,
        page,
        partnershipTop.getPages().size());
    List<PlayerDataPair> listPage = partnershipTop.getPage(page);
    int slot = partnershipTop.getEntryStartedOfPage(page);
    for (int i = 0; i < listPage.size(); i++, slot++) {
      PlayerDataPair pair = listPage.get(i);
      if (CommonsUtils.isNullOrEmpty(page)) {
        MessageUtils.compose(sender,
            Messages.LIST_PARTNER_ENTRY_EMPTY,
            false,
            slot);
      }
      String playerName1 = processPlayerData(pair.getLeft());
      String playerName2 = processPlayerData(pair.getRight());
      MessageUtils.compose(sender,
          Messages.LIST_PARTNER_ENTRY,
          false,
          slot,
          playerName1,
          playerName2);
    }
  }

  private String processPlayerData(PlayerData data) {
    return data == null
        ? "N/A"
        : data.getGender().getChatColor() + data.getName();
  }

  @Command(
      names = {"genders", "gender", "g"},
      desc = "%translatable:list.genders.desc%")
  public void genders(CommandSender sender,
                      @OptArg @Sender MargaretPlayer margaretPlayer,
                      GenderManager genderManager) {
    List<Gender> genders = Lists
        .newArrayList(genderManager.getGenders());
    MessageUtils.compose(sender,
        Messages.LIST_GENDERS_HEADER,
        true,
        genders.size());
    for (Gender gender : genders) {
      MessageUtils.compose(sender,
          Messages.LIST_ENTRY,
          false,
          gender.getFormalNameColorized());
    }
    if (!margaretPlayer.isEmpty()) {
      Gender gender = margaretPlayer.getGender();
      MessageUtils.compose(margaretPlayer,
          Messages.CURRENT_GENDER,
          false,
          gender.getFormalNameColorized());
    }
  }

  @Command(
      names = {"proposals", "proposal", "prop"},
      desc = "%translatable:list.proposals.desc%")
  public void proposals(CommandSender sender,
                        @OptArg MargaretPlayer margaretPlayer) {
    if (margaretPlayer.isEmpty()) {
      checkInstanceOfPlayer(sender, Messages.NEEDS_ARGUMENT);
      margaretPlayer = MargaretPlayers
          .getAsMargaretPlayer(sender);
    }

    sendProposals(sender, margaretPlayer);
  }

  private void sendProposals(CommandSender sender, MargaretPlayer margaretPlayer) {
    Set<Proposal> proposals = margaretPlayer.getProposals();
    MessageUtils.compose(sender,
        Messages.LIST_PROPOSALS_HEADER,
        true,
        proposals.size());
    for (Proposal proposal : proposals) {
      MessageUtils.compose(sender,
          Messages.LIST_ENTRY,
          false,
          MargaretPlayers.getNameColorized(
              proposal.getSender()));
    }
  }

  @Command(
      names = {"homes", "h"},
      desc = "%translatable:list.homes.desc%")
  public void homes(CommandSender sender,
                    @Sender Partnership partnership) {
    HomeList homes = partnership.getHomeList();
    MessageUtils.compose(sender,
        Messages.LIST_HOME_HEADER,
        true,
        homes.size());
    for (int i = 0; i < homes.size(); i++) {
      Home home = homes.get(i);
      int entry = i + 1;
      Messages message;
      // I do not like this.
      Object[] objects = new Object[3];
      objects[0] = entry;
      objects[1] = home.getId();
      if (!home.hasAlias()) {
        message = Messages.LIST_HOME_ENTRY;
      } else {
        message = Messages.LIST_HOME_WITH_ALIAS_ENTRY;
        objects[2] = home.getAlias();
      }
      MessageUtils.compose(
          sender,
          message,
          false,
          objects);
    }
  }

  @Command(
      names = {"settings"},
      desc = "%translatable:list.settings.desc%")
  public void settings(CommandSender sender) {
    MargaretPlayer margaretPlayer = MargaretPlayers
        .getAsMargaretPlayer(sender);
    Collection<EnumSetting> values = PlayerSettings
        .getAllSettings().values();
    MessageUtils.compose(sender,
        Messages.LIST_SETTINGS_HEADER,
        true,
        values.size());
    for (EnumSetting setting : values) {
      boolean isSetting = margaretPlayer.isSetting(setting);
      String settingName = setting.getFormalName();
      if (!margaretPlayer.isEmpty()) {
        ChatColor colorized = isSetting
            ? ChatColor.GREEN
            : ChatColor.RED;
        settingName = colorized + settingName;
      }
      TextComponent component = TextComponent.of(settingName)
          .clickEvent(
              ClickEvent.runCommand(
                  "/mr setting " + settingName))
          .hoverEvent(
              MessageUtils.hoverTextOf(
                  Messages.LIST_SETTING_ENTRY_HOVER,
                  settingName, isSetting));
      MessageUtils.composeInteractive(sender,
          Messages.LIST_ENTRY,
          false,
          component);
    }
  }

  @Command(
      names = {"relations", "rt"},
      desc = "%translatable:list.relations.desc%")
  public void relations(CommandSender sender) {
    List<String> relations = MargaretYamlStorage.getAllowedRelations();
    MessageUtils.compose(sender,
        Messages.LIST_RELATIONS_HEADER,
        true,
        relations.size());
    for (String relation : relations) {
      MessageUtils.compose(sender,
          Messages.LIST_ENTRY,
          false,
          relation);
    }
  }
}
