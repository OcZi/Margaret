package me.oczi.bukkit.internal.commandmanager;

import app.ashcon.intake.*;
import app.ashcon.intake.bukkit.BukkitIntake;
import app.ashcon.intake.dispatcher.Dispatcher;
import app.ashcon.intake.fluent.CommandGraph;
import app.ashcon.intake.util.auth.AuthorizationException;
import com.google.common.base.Joiner;
import me.oczi.bukkit.other.exceptions.ConditionException;
import me.oczi.bukkit.other.exceptions.InvocationConditionException;
import me.oczi.bukkit.utils.MessageUtils;
import me.oczi.bukkit.utils.Messages;
import me.oczi.common.utils.CommonsUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class MargaretIntake extends BukkitIntake {

  public MargaretIntake(Plugin plugin, CommandGraph commandGraph) {
    super(plugin, commandGraph);
  }

  @Override
  public boolean onCommand(CommandSender sender,
                           Command command,
                           String label,
                           String[] args) {
    Dispatcher dispatcher = getCommandGraph()
        .getRootDispatcherNode()
        .getDispatcher();
    try {
      return dispatcher
          .call(getCommand(command, args),
                getNamespace(sender));
    } catch (InvalidUsageException e) {
      if (e.getMessage() != null) {
        MessageUtils.compose(sender, e.getMessage(), true);
      }

      if (e.isFullHelpSuggested()) {
        String aliases = Joiner
            .on(' ')
            .skipNulls()
            .join(e.getAliasStack());
        String usage = MargaretCommandManager
            .getUsageOf(getLastIndex(e.getAliasStack()));
        MessageUtils.compose(sender,
            Messages.USAGE_ENTRY,
            true,
            aliases,
            usage);
        sendSuggestions(sender, e.getCommand(), args);
      }
    } catch (AuthorizationException e) {
      List<String> permissions = dispatcher.get(command.getName())
          .getDescription()
          .getPermissions();
      MessageUtils.compose(sender,
          Messages.PLAYER_NO_PERMISSION,
          true);
    } catch (InvocationCommandException e) {
      Throwable cause = e.getCause();
      if (cause instanceof InvocationConditionException) {
        MessageUtils.compose(sender, cause.getCause().getMessage(), true);
      } else {
        MessageUtils.compose(sender, e.getMessage(), true);
        e.printStackTrace();
      }
    } catch (CommandException e) {
      MessageUtils.compose(sender, e.getMessage(), true);
      if (!(e instanceof ConditionException)) {
        e.printStackTrace();
      }
    }

    return true;
  }

  private String getLastIndex(List<String> list) {
    return list.get(list.size() - 1);
  }

  public void sendSuggestions(CommandSender sender,
                              CommandCallable command,
                              String[] args) {
    List<String> suggestions = null;
    try {
      suggestions = command.getSuggestions(
          args[args.length - 1], getNamespace(sender));
    } catch (CommandException ex) {
      ex.printStackTrace();
    }
    if (!CommonsUtils.isNullOrEmpty(suggestions)) {
      String suggestion = String.join(", ", suggestions);
      MessageUtils.compose(sender,
          Messages.SUGGESTIONS,
          true,
          suggestion);
    }
  }
}
