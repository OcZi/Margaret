package me.oczi.bukkit.commands;

import app.ashcon.intake.Command;
import app.ashcon.intake.bukkit.parametric.annotation.Sender;
import app.ashcon.intake.dispatcher.Dispatcher;
import app.ashcon.intake.parametric.annotation.Default;
import com.google.common.collect.Lists;
import me.oczi.bukkit.internal.MemoryManager;
import me.oczi.bukkit.internal.commandmanager.CommandManager;
import me.oczi.bukkit.objects.Gender;
import me.oczi.bukkit.objects.Home;
import me.oczi.bukkit.objects.Proposal;
import me.oczi.bukkit.objects.collections.HomeList;
import me.oczi.bukkit.objects.collections.PartnershipTop;
import me.oczi.bukkit.objects.partnership.Partnership;
import me.oczi.bukkit.objects.player.MargaretPlayer;
import me.oczi.bukkit.objects.player.PlayerData;
import me.oczi.bukkit.objects.player.PlayerDataPair;
import me.oczi.bukkit.other.exceptions.ConditionException;
import me.oczi.bukkit.storage.yaml.MargaretYamlStorage;
import me.oczi.bukkit.utils.*;
import me.oczi.bukkit.utils.settings.EnumSettings;
import me.oczi.bukkit.utils.settings.PlayerSettings;
import me.oczi.common.utils.CommonsUtils;
import net.kyori.text.TextComponent;
import net.kyori.text.event.ClickEvent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static me.oczi.bukkit.utils.CommandPreconditions.*;

public class CommandList {

  @Command(
      aliases = {"help", "?", ""},
      desc = "List command.")
  public void mainCommand(@Sender CommandSender sender,
                          CommandManager commandManager,
                          @Default("") String arg) {
    Dispatcher dispatcher = commandManager
        .getListNode().getDispatcher();
    Commands.composeFullHelp(sender,
        dispatcher,
        "list",
        "list",
        true);
  }

  @Command(
      aliases = {"top-partner", "partners", "partner"},
      desc = "Top partners.")
  public void partners(@Sender CommandSender sender,
                       MemoryManager memoryManager,
                       @Default("") String numPage) {
    PartnershipTop partnershipTop = memoryManager.getPartnerTop();
    int page = CommonsUtils.isNullOrEmpty(numPage)
        ? 1
        : CommonsUtils.parseInt(numPage);
    if (page <= 0)  {
      page = 1;
    }
    MessageUtils.compose(sender,
        Messages.LIST_PARTNER_HEADER,
        true,
        page);
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
      aliases = {"genders", "gender", "g"},
      desc = "List of genders.")
  public void genders(@Sender CommandSender sender,
                      GenderManager genderManager) {
    MargaretPlayer margaretPlayer = MargaretPlayers
        .getAsMargaretPlayer(sender);
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
      aliases = {"proposals", "proposal", "prop"},
      desc = "List of proposals.")
  public void proposals(@Sender CommandSender sender,
                        @Default("") String playerName)
      throws ConditionException {
    MargaretPlayer margaretPlayer;
    if (playerName.isEmpty()) {
      checkInstanceOfPlayer(sender, Messages.NEEDS_ARGUMENT);
      margaretPlayer = MargaretPlayers
          .getAsMargaretPlayer(sender);
    } else {
      margaretPlayer = MargaretPlayers
          .getAsMargaretPlayer(playerName);
      checkMargaretPlayerOnline(margaretPlayer, playerName);
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
      aliases = {"homes", "h"},
      desc = "List of homes.")
  public void homes(@Sender CommandSender sender)
      throws ConditionException {
    MargaretPlayer margaretPlayer = MargaretPlayers
        .getAsMargaretPlayer(sender);
    checkHavePartner(margaretPlayer);

    Partnership partnership = margaretPlayer.getPartnership();
    HomeList homes = partnership.getHomeList();
    MessageUtils.compose(sender,
        Messages.LIST_HOME_HEADER,
        true,
        homes.size());
    for (int i = 0; i < homes.size(); i++) {
      Home home = homes.get(i);
      String homeInfo = home.hasAlias()
          ? home.getId() + " (" + home.getAlias() + ")"
          : home.getId();
      MessageUtils.compose(sender,
          Messages.LIST_ENUM_ENTRY,
          false,
          i + 1,
          "ID: " + homeInfo);
    }
  }

  @Command(
      aliases = {"settings"},
      desc = "List of settings.")
  public void settings(@Sender CommandSender sender) {
    MargaretPlayer margaretPlayer = MargaretPlayers
        .getAsMargaretPlayer(sender);
    Collection<EnumSettings> values = PlayerSettings
        .getAllSettings().values();
    MessageUtils.compose(sender,
        Messages.LIST_SETTINGS_HEADER,
        true,
        values.size());
    for (EnumSettings setting : values) {
      boolean isSetting = margaretPlayer.isSetting(setting);
      String settingName = setting.getName()
          .replace("_", "-");
      if (!margaretPlayer.isEmpty()) {
        settingName = (isSetting
            ? ChatColor.GREEN
            : ChatColor.RED)
            + settingName;
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
      aliases = {"relations", "rt"},
      desc = "List of relations.")
  public void relations(@Sender CommandSender sender) {
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
