package me.oczi.bukkit.commands;

import app.ashcon.intake.Command;
import app.ashcon.intake.bukkit.parametric.annotation.Sender;
import app.ashcon.intake.dispatcher.Dispatcher;
import app.ashcon.intake.parametric.annotation.Default;
import me.oczi.bukkit.internal.commandmanager.CommandManager;
import me.oczi.bukkit.objects.Gender;
import me.oczi.bukkit.objects.partnership.Partnership;
import me.oczi.bukkit.objects.player.MargaretPlayer;
import me.oczi.bukkit.other.exceptions.ConditionException;
import me.oczi.bukkit.utils.Commands;
import me.oczi.bukkit.utils.MargaretPlayers;
import me.oczi.bukkit.utils.MessageUtils;
import me.oczi.bukkit.utils.Messages;
import me.oczi.bukkit.utils.settings.EnumSettings;
import me.oczi.bukkit.utils.settings.PartnershipSettings;
import org.bukkit.command.CommandSender;

import static me.oczi.bukkit.utils.CommandPreconditions.checkInstanceOfPlayer;

public class CommandMain {

  // Generic command help.
  @Command(
      aliases = {"help", "?", ""},
      desc = "Help command.")
  public void mainCommand(@Sender CommandSender sender,
                          CommandManager commandManager,
                          @Default("") String arg) {
    Dispatcher dispatcher = commandManager.getDispatcher();
    Commands.composeFullHelp(sender,
        dispatcher,
        "margaret",
        "help",
        false);
  }

  @Command(
      aliases = {"gender", "g"},
      desc = "Set your gender.",
      perms = "margaret.gender")
  public void gender(@Sender CommandSender sender,
                     Gender gender)
      throws ConditionException {
    checkInstanceOfPlayer(sender);
    MargaretPlayer margaretPlayer = MargaretPlayers
        .getAsMargaretPlayer(sender);
    MargaretPlayers.setGender(margaretPlayer, gender);
    MessageUtils.compose(sender,
        Messages.SET_GENDER,
        true,
        gender.getFormalNameColorized());
  }

  @Command(
      aliases = {"setting", "option"},
      desc = "Toggle settings.",
      perms = "margaret.setting")
  public void setting(@Sender CommandSender sender,
                      EnumSettings setting)
      throws ConditionException {
    checkInstanceOfPlayer(sender);
    MargaretPlayer margaretPlayer = MargaretPlayers
        .getAsMargaretPlayer(sender);
    if (setting instanceof PartnershipSettings) {
      if (!margaretPlayer.havePartner()) {
        MessageUtils.compose(sender,
            Messages.SETTING_PARTNER_ONLY,
            true);
        return;
      } else {
        Partnership partnership = margaretPlayer.getPartnership();
        if (!partnership.hasPermission(setting.getPermissionEquivalent())) {
          MessageUtils.compose(sender,
              Messages.SETTING_PARTNER_NOT_APPLY,
              true);
          return;
        }
      }
    }
    boolean result = MargaretPlayers.toggleSetting(
        margaretPlayer, setting);

    MessageUtils.compose(margaretPlayer,
        Messages.SETTING_ENTRY,
        true,
        setting.getFormalName(),
        result);
  }
}
