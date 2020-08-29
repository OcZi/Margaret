package me.oczi.bukkit.utils;

import app.ashcon.intake.CommandMapping;
import app.ashcon.intake.dispatcher.Dispatcher;
import com.google.common.base.Strings;
import net.kyori.text.TextComponent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

public interface Commands {


  static String getDescFormatted(CommandMapping command) {
    String desc = command.getDescription().getShortDescription();
    if (!Strings.isNullOrEmpty(desc)) {
      return desc;
    }
    desc = StringUtils.capitalize(command.getPrimaryAlias());
    return MessageUtils.composeMessageStripped(
        Messages.UNKNOWN_DESC, false, desc);
  }

  static void composeGenericHelpIndex(CommandSender sender,
                                      String node,
                                      Dispatcher dispatcher) {
    for (CommandMapping command : dispatcher.getCommands()) {
      String aliases = command.getPrimaryAlias();
      String desc = Commands.getDescFormatted(command);
      MessageUtils.compose(sender,
          Messages.COMMAND_LIST_ENTRY,
          false,
          node,
          aliases,
          desc);
    }
  }

  static void composeInteractiveHelpIndex(CommandSender sender,
                                          String firstNode,
                                          String node,
                                          Dispatcher dispatcher) {
    for (CommandMapping command : dispatcher.getCommands()) {
      TextComponent componentNode = TextComponent
          .of("... ")
          .hoverEvent(MessageUtils
              .hoverTextOf(Messages.HIDDEN_NODE, firstNode))
          .append(TextComponent.of(node));
      TextComponent aliases = TextComponent.of(
          command.getPrimaryAlias());
      TextComponent desc = TextComponent.of(
          Commands.getDescFormatted(command));
      MessageUtils.composeInteractive(sender,
          Messages.COMMAND_LIST_ENTRY,
          false,
          componentNode,
          aliases,
          desc);
    }
  }

  static void composeFullHelp(CommandSender sender,
                              Dispatcher dispatcher,
                              String node,
                              String helpName,
                              boolean interactive,
                              String... extraMessages) {
    MessageUtils.compose(sender,
        Messages.COMMAND_LIST_PAGE_HEADER,
        false,
        "Margaret",
        helpName);
    if (interactive) {
      // Always the first node will be margaret... for now.
      composeInteractiveHelpIndex(
          sender, "margaret", node, dispatcher);
    } else {
      composeGenericHelpIndex(
          sender, node, dispatcher);
    }
    for (String extraMessage : extraMessages) {
      if (!Strings.isNullOrEmpty(extraMessage)) {
        MessageUtils.compose(sender, extraMessage, false);
      }
    }
    MessageUtils.compose(sender,
        Messages.COMMAND_LIST_PAGE_FOOTER,
        false);
  }
}
