package me.oczi.bukkit.commands;

import app.ashcon.intake.Command;
import app.ashcon.intake.bukkit.parametric.annotation.Sender;
import app.ashcon.intake.dispatcher.Dispatcher;
import app.ashcon.intake.parametric.annotation.Default;
import me.oczi.bukkit.internal.commandmanager.CommandManager;
import me.oczi.bukkit.objects.Gender;
import me.oczi.bukkit.objects.collections.PartnershipPermissionSet;
import me.oczi.bukkit.objects.partnership.Partnership;
import me.oczi.bukkit.objects.player.MargaretPlayer;
import me.oczi.bukkit.other.exceptions.ConditionException;
import me.oczi.bukkit.utils.*;
import me.oczi.bukkit.utils.settings.EnumSettings;
import org.bukkit.command.CommandSender;

import static me.oczi.bukkit.utils.CommandPreconditions.*;

public class CommandAdmin {

  @Command(
      aliases = {"help", "?", ""},
      desc = "Admin command.")
  public void mainCommand(@Sender CommandSender sender,
                          CommandManager commandManager,
                          @Default("") String arg) {
    Dispatcher dispatcher = commandManager
        .getAdminNode().getDispatcher();
    Commands.composeFullHelp(sender,
        dispatcher,
        "admin",
        "admin",
        true);
  }

  @Command(
      aliases = {"op-partner", "op-partnership"},
      desc = "Op partnership.",
      perms = "margaret.admin.op-partnership")
  public void op(@Sender CommandSender sender)
      throws ConditionException {
    checkInstanceOfPlayer(sender);
    MargaretPlayer margaretPlayer = MargaretPlayers
        .getAsMargaretPlayer(sender);
    checkHavePartner(margaretPlayer);

    Partnership partnership = margaretPlayer.getPartnership();
    PartnershipPermissionSet permissions = partnership.getPermissions();
    permissions.setPermissions(PartnershipPermission.class);
    MessageUtils.compose(sender,
        Messages.ALL_PERMISSION_ADDED,
        true);
  }

  @Command(
      aliases = {"partner-info", "partnership-info", "p-info"},
      desc = "Partner information.")
  public void partnerInfo(@Sender CommandSender sender,
                          MargaretPlayer margaretPlayer)
      throws ConditionException {
    checkHavePartner(margaretPlayer,
        Messages.PLAYER_NOT_HAVE_PARTNER,
        margaretPlayer.getName());
    Partnerships.sendInfo(sender, margaretPlayer);
  }

  @Command(
      aliases = "max-homes",
      desc = "Set max homes of partner.")
  public void setMaxHomes(@Sender CommandSender sender,
                          int i,
                          @Default("") String partnerId)
      throws ConditionException {
    if (partnerId.isEmpty()) {
      checkInstanceOfPlayer(sender, Messages.NEEDS_ARGUMENT);
      MargaretPlayer margaretPlayer = MargaretPlayers
          .getAsMargaretPlayer(sender);
      checkHavePartner(margaretPlayer);
      setMaxHomesOf(margaretPlayer.getPartnership(), i);
    } else {
      Partnership partnership = Partnerships.getAsPartner(partnerId);
      checkIsEmpty(partnership, Messages.INVALID_PARTNER, partnerId);
      setMaxHomesOf(partnership, i);
      MessageUtils.compose(sender,
          Messages.MAX_HOMES_SET_TO,
          true,
          i);
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
      aliases = "force-gender",
      desc = "Set gender of player.",
      perms = "margaret.admin.force-gender")
  public void setGender(@Sender CommandSender sender,
                        MargaretPlayer margaretPlayer,
                        Gender gender) {
    MargaretPlayers.setGender(margaretPlayer, gender);
    MessageUtils.compose(sender,
        Messages.SET_GENDER_OF,
        true,
        margaretPlayer.getName(),
        gender.getFormalNameColorized());
  }

  @Command(
      aliases = "force-setting",
      desc = "Toggle setting of player.",
      perms = "margaret.admin.force-setting")
  public void toggleSetting(@Sender CommandSender sender,
                            MargaretPlayer margaretPlayer,
                            EnumSettings setting) {
    boolean result = MargaretPlayers
        .toggleSetting(margaretPlayer, setting);
    MessageUtils.compose(sender,
        Messages.SETTING_ENTRY_OF,
        true,
        margaretPlayer.getName(),
        setting.getName(),
        result);
  }
}
