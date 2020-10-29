package me.oczi.bukkit.commands;

import app.ashcon.intake.Command;
import app.ashcon.intake.bukkit.parametric.annotation.Sender;
import app.ashcon.intake.dispatcher.Dispatcher;
import app.ashcon.intake.parametric.annotation.Default;
import me.oczi.bukkit.internal.commandmanager.CommandManager;
import me.oczi.bukkit.objects.player.MargaretPlayer;
import me.oczi.bukkit.other.exceptions.ConditionException;
import me.oczi.bukkit.utils.*;
import org.bukkit.command.CommandSender;

import static me.oczi.bukkit.utils.CommandPreconditions.checkHavePartner;
import static me.oczi.bukkit.utils.CommandPreconditions.checkInstanceOfPlayer;

public class CommandPartnership {

  // Generic command help.
  @Command(
      aliases = {"help", "?", ""},
      desc = "Partnership command.")
  public void mainCommand(@Sender CommandSender sender,
                          CommandManager commandManager,
                          @Default("") String arg) {
    Dispatcher dispatcher = commandManager
        .getPartnerNode().getDispatcher();
    MargaretPlayer margaretPlayer = MargaretPlayers
        .getAsMargaretPlayer(sender);
    Commands.composeFullHelp(sender,
        dispatcher,
        "partner",
        "partnership",
        true,
        partnerExtraMessage(margaretPlayer));
  }

  private String partnerExtraMessage(MargaretPlayer margaretPlayer) {
    String partnerName = !margaretPlayer.isEmpty()
        ? Partnerships.getNameOfPartner(margaretPlayer, true)
        : MessageUtils.getMessageTranslated(Messages.NONE);
    return MessageUtils.composeMessage(
        Messages.CURRENT_PARTNER,
        false,
        partnerName);
  }

  @Command(
      aliases = {"information", "info"},
      desc = "See the information of your partnership.",
      perms = "margaret.partnership.info")
  public void info(@Sender CommandSender sender)
      throws ConditionException {
    checkInstanceOfPlayer(sender,
        Messages.NEEDS_ARGUMENT);
    MargaretPlayer margaretPlayer = MargaretPlayers
        .getAsMargaretPlayer(sender);
    checkHavePartner(margaretPlayer);
    Partnerships.sendInfo(sender, margaretPlayer);
  }

  @Command(
      aliases = {"end", "divorce"},
      desc = "End your partnership.")
  public void end(@Sender CommandSender sender)
      throws ConditionException {
    checkInstanceOfPlayer(sender);
    MargaretPlayer margaretPlayer = MargaretPlayers
        .getAsMargaretPlayer(sender);
    checkHavePartner(margaretPlayer);
    Partnerships.endPartner(margaretPlayer.getPartnership());
  }
}

