package me.oczi.bukkit.internal.commandflow.components;

import me.fixeddev.commandflow.CommandManager;
import me.fixeddev.commandflow.Namespace;
import me.fixeddev.commandflow.NamespaceImpl;
import me.fixeddev.commandflow.bukkit.BukkitCommandManager;
import me.fixeddev.commandflow.bukkit.BukkitCommandWrapper;
import me.fixeddev.commandflow.command.Command;
import me.fixeddev.commandflow.exception.*;
import me.fixeddev.commandflow.translator.Translator;
import me.fixeddev.commandflow.usage.UsageBuilder;
import me.oczi.bukkit.utils.Commands;
import me.oczi.bukkit.utils.MessageUtils;
import net.kyori.text.Component;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MargaretCommandWrapper extends BukkitCommandWrapper {
  private final CommandManager commandManager;
  private final Translator translator;

  public MargaretCommandWrapper(Command command,
                                CommandManager dispatcher,
                                Translator translator) {
    super(command, dispatcher, translator);
    this.commandManager = dispatcher;
    this.translator = translator;
  }

  @Override
  public boolean execute(CommandSender sender, String label, String[] args) {
    List<String> argumentLine = new ArrayList<>();

    argumentLine.add(getLabel());
    argumentLine.addAll(Arrays.asList(args));

    Namespace namespace = new NamespaceImpl();
    namespace.setObject(CommandSender.class, BukkitCommandManager.SENDER_NAMESPACE, sender);

    try {
      return commandManager
          .execute(namespace, argumentLine);
    } catch (CommandUsage e) {
      CommandException exceptionToSend = e;
      boolean usagePart = false;
      if (e.getCause() instanceof ArgumentParseException) {
        exceptionToSend = (ArgumentParseException) e.getCause();
        usagePart = true;
      }
      sendMessageToSender(exceptionToSend, sender, namespace);

      if (usagePart) {
        return true;
      }
      List<String> suggestions = new ArrayList<>();
      try {
        suggestions.addAll(
            commandManager
                .getSuggestions(
                    namespace, argumentLine));
      } catch (NoMoreArgumentsException ignored) {
      }

      List<String> executionPath =
          getExecutionPathFromUsage(e);
      Commands.composeHelpCommand(
          sender,
          e.getCommand(),
          executionPath,
          suggestions);
      return true;
    } catch (InvalidSubCommandException e) {
      sendMessageToSender(e, sender, namespace);
      throw new org.bukkit.command.CommandException(
          "An internal parse exception occurred while executing the command " + getLabel(), e);
    } catch (ArgumentParseException | NoMoreArgumentsException e) {
      sendMessageToSender(e, sender, namespace);
    } catch (NoPermissionsException e) {
      sendMessageToSender(e,
          sender,
          namespace,
          e.getCommand().getPermission());
      return true;
    } catch (CommandException e) {
      CommandException exceptionToSend = e;

      if (e.getCause() instanceof CommandException) {
        exceptionToSend = (CommandException) e.getCause();
      }

      sendMessageToSender(exceptionToSend, sender, namespace);
      throw new org.bukkit.command.CommandException(
          "An unexpected exception occurred while executing the command " + getLabel(), exceptionToSend);
    }

    return false;
  }

  private void sendMessageToSender(CommandException exception,
                                   CommandSender sender,
                                   Namespace namespace,
                                   Object... objects) {
    Component component = exception.getMessageComponent();
    Component translate = translator.translate(component, namespace);
    MessageUtils.compose(sender, translate, true, objects);
  }

  /**
   * Hack method to get Execution Path
   * from the usage message component.
   *
   * Created because this class not have access to the most recent CommandContext,
   * but the message generated by {@link UsageBuilder} will expose them.
   * @param usage Command usage to get execution path.
   * @return Execution path as String list.
   */
  private List<String> getExecutionPathFromUsage(CommandUsage usage) {
    String message = MessageUtils.toLegacy(
        usage.getMessageComponent());
    message = message
        .substring(
            // Get start of execution path
            // by slash.
            message.indexOf("/") + 1,
            // Get end of execution path
            // by the start of their arguments.
            message.indexOf(" <"));
    return Arrays.asList(
        message.split(" "));
  }
}