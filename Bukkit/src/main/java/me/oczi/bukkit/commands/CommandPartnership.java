package me.oczi.bukkit.commands;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.OptArg;
import me.fixeddev.commandflow.annotated.annotation.SubCommandClasses;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import me.oczi.bukkit.internal.commandflow.CommandFlow;
import me.oczi.bukkit.objects.partnership.Partnership;
import me.oczi.bukkit.objects.player.MargaretPlayer;
import me.oczi.bukkit.other.exceptions.ConditionException;
import me.oczi.bukkit.utils.Commands;
import me.oczi.bukkit.utils.MessageUtils;
import me.oczi.bukkit.utils.Messages;
import me.oczi.bukkit.utils.Partnerships;
import org.bukkit.command.CommandSender;

@Command(
    names = {"partner", "partnership", "pr"},
    desc = "%translatable:partner.desc%",
    permission = "margaret.partnership")
@SubCommandClasses({SubCommandPartnership.class, CommandHome.class})
public class CommandPartnership implements CommandClass {

  // Generic command help.
  @Command(
      names = {"help", "?"},
      desc = "%translatable:partner.help.desc%")
  public void mainCommand(CommandSender sender,
                          @OptArg @Sender MargaretPlayer margaretPlayer,
                          CommandFlow commandFlow) {
    Commands.composeFullChildrenHelp(sender,
        commandFlow.getSubCommandsOf("partner"),
        "margaret",
        "partner",
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
      names = {"information", "info"},
      desc = "%translatable:partnership.information.desc%",
      permission = "margaret.partnership.info")
  public void info(CommandSender sender,
                   @Sender MargaretPlayer margaretSender,
                   // Pass partnership check
                   @Sender Partnership partnershipSender)
      throws ConditionException {
    Partnerships.sendInfo(sender, margaretSender);
  }

  @Command(
      names = {"end", "divorce"},
      desc = "%translatable:partnership.end.desc%")
  public void end(@Sender Partnership partnershipSender)
      throws ConditionException {
    Partnerships.endPartner(partnershipSender);
  }
}

