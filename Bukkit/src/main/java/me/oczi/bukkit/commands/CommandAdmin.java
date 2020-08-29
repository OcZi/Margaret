package me.oczi.bukkit.commands;

import app.ashcon.intake.Command;
import app.ashcon.intake.bukkit.parametric.annotation.Sender;
import app.ashcon.intake.dispatcher.Dispatcher;
import app.ashcon.intake.parametric.annotation.Default;
import me.oczi.bukkit.internal.commandmanager.CommandManager;
import me.oczi.bukkit.objects.Gender;
import me.oczi.bukkit.objects.collections.PartnerPermissionSet;
import me.oczi.bukkit.objects.partner.Partner;
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
      aliases = "op-partner",
      desc = "Op partner.",
      perms = "margaret.admin.op-partner")
  public void op(@Sender CommandSender sender)
      throws ConditionException {
    checkInstanceOfPlayer(sender);
    MargaretPlayer margaretPlayer = MargaretPlayers
        .getAsMargaretPlayer(sender);
    checkHavePartner(margaretPlayer);

    Partner partner = margaretPlayer.getPartner();
    PartnerPermissionSet permissions = partner.getPermissions();
    permissions.setPermissions(PartnerPermission.class);
    MessageUtils.compose(sender,
        Messages.ALL_PERMISSION_ADDED,
        true);
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
      setMaxHomesOf(margaretPlayer.getPartner(), i);
    } else {
      Partner partner = Partners.getAsPartner(partnerId);
      checkIsEmpty(partner, Messages.INVALID_PARTNER, partnerId);
      setMaxHomesOf(partner, i);
      MessageUtils.compose(sender,
          Messages.MAX_HOMES_SET_TO,
          true,
          i);
    }
  }

  private void setMaxHomesOf(Partner partner, int i) {
    partner.getHomeList().setMaxHomes(i);
    MessageUtils.compose(partner,
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
