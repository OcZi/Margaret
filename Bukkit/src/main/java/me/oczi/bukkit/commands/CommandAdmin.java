package me.oczi.bukkit.commands;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.OptArg;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import me.oczi.bukkit.internal.commandflow.CommandFlow;
import me.oczi.bukkit.objects.Gender;
import me.oczi.bukkit.objects.collections.PartnershipPermissionSet;
import me.oczi.bukkit.objects.partnership.Partnership;
import me.oczi.bukkit.objects.player.MargaretPlayer;
import me.oczi.bukkit.other.exceptions.ConditionException;
import me.oczi.bukkit.utils.*;
import me.oczi.bukkit.utils.settings.EnumSetting;
import org.bukkit.command.CommandSender;

import static me.oczi.bukkit.utils.CommandPreconditions.*;

@Command(
    names = {"admin", "ad"},
    desc = "%translatable:admin.desc%",
    permission = "margaret.admin")
public class CommandAdmin implements CommandClass {

  @Command(
      names = {"help", "?", ""},
      desc = "%translatable:admin.help.desc%")
  public void mainCommand(CommandSender sender,
                          CommandFlow commandFlow) {
    Commands.composeFullChildrenHelp(sender,
        commandFlow.getSubCommandsOf("admin"),
        "margaret",
        "admin");
  }

  @Command(
      names = {"op-partnership", "op-partner"},
      desc = "%translatable:admin.op.partnership.desc%",
      permission = "margaret.admin.op-partnership")
  public void op(@Sender MargaretPlayer sender,
                 @Sender Partnership partnershipSender)
      throws ConditionException {
    PartnershipPermissionSet permissions =
        partnershipSender.getPermissions();
    permissions.setPermissions(PartnershipPermission.class);
    MessageUtils.compose(sender,
        Messages.ALL_PERMISSION_ADDED,
        true);
  }

  @Command(
      names = {"partnership-info", "partner-info", "p-info"},
      desc = "%translatable:admin.partnership.info.desc%")
  public void partnerInfo(CommandSender sender,
                          MargaretPlayer player)
      throws ConditionException {
    checkHavePartner(player,
        Messages.PLAYER_NOT_HAVE_PARTNER,
        player.getName());
    Partnerships.sendInfo(sender, player);
  }

  @Command(
      names = {"max-homes"},
      desc = "%translatable:admin.max.homes.desc%",
      permission = "margaret.admin.max-homes")
  public void setMaxHomes(CommandSender sender,
                          int maxHomes,
                          @OptArg("") String partnershipId)
      throws ConditionException {
    if (partnershipId.isEmpty()) {
      checkInstanceOfPlayer(sender, Messages.NEEDS_ARGUMENT);
      MargaretPlayer margaretPlayer = MargaretPlayers
          .getAsMargaretPlayer(sender);
      checkHavePartner(margaretPlayer);
      setMaxHomesOf(margaretPlayer.getPartnership(), maxHomes);
    } else {
      Partnership partnership = Partnerships.getAsPartner(partnershipId);
      checkIsEmpty(partnership, Messages.INVALID_PARTNER, partnershipId);
      setMaxHomesOf(partnership, maxHomes);
      MessageUtils.compose(sender,
          Messages.MAX_HOMES_SET_TO,
          true,
          maxHomes);
    }
  }

  private void setMaxHomesOf(Partnership partnership, int i) {
    partnership.getHomeList().setMaxHomes(i);
    MessageUtils.compose(partnership,
        Messages.MAX_HOMES_CHANGED,
        true,
        i);
  }

  @Command(
      names = "force-gender",
      desc = "%translatable:admin.force.gender.desc%",
      permission = "margaret.admin.force-gender")
  public void setGender(CommandSender sender,
                        MargaretPlayer player,
                        Gender gender) {
    MargaretPlayers.setGender(player, gender);
    MessageUtils.compose(sender,
        Messages.SET_GENDER_OF,
        true,
        player.getName(),
        gender.getFormalNameColorized());
  }

  @Command(
      names = "force-setting",
      desc = "%translatable:admin.force.setting.desc%",
      permission = "margaret.admin.force-setting")
  public void toggleSetting(MargaretPlayer player,
                            EnumSetting setting) {
    boolean result = MargaretPlayers
        .toggleSetting(player, setting);
    MessageUtils.compose(player,
        Messages.SETTING_ENTRY_OF,
        true,
        player.getName(),
        setting.getName(),
        result);
  }
}
