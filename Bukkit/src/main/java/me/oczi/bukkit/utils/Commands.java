package me.oczi.bukkit.utils;

import me.fixeddev.commandflow.Namespace;
import me.fixeddev.commandflow.bukkit.BukkitCommandManager;
import me.fixeddev.commandflow.command.Command;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.part.PartsWrapper;
import me.fixeddev.commandflow.part.SinglePartWrapper;
import me.fixeddev.commandflow.part.defaults.SubCommandPart;
import me.fixeddev.commandflow.translator.Translator;
import me.oczi.bukkit.MargaretMain;
import me.oczi.bukkit.PluginCore;
import me.oczi.bukkit.internal.commandflow.CommandFlow;
import me.oczi.bukkit.objects.player.MargaretPlayer;
import me.oczi.common.utils.CommonsUtils;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public interface Commands {

  static CommandSender toSender(Namespace namespace) {
    return namespace.getObject(CommandSender.class,
        BukkitCommandManager.SENDER_NAMESPACE);
  }

  static void composeHelpCommand(CommandSender sender,
                                 Command command,
                                 List<String> labels,
                                 List<String> suggestions) {
    if (!CommonsUtils.isNullOrEmpty(suggestions)) {
      MessageUtils.compose(sender,
          Messages.SUGGESTIONS,
          true,
          suggestions);
    }
    Set<Command> subCommands = getSubCommandsOf(
        command.getName());
    if (CommonsUtils.isNullOrEmpty(subCommands)) {
      return;
    }
    labels = new ArrayList<>(labels);
    for (Command subCommand : subCommands) {
      String subCommandName = subCommand.getName();
      if (subCommandName.equals("help")) {
        labels.add(subCommandName);
        MessageUtils.compose(sender,
            Messages.MORE_HELP,
            true,
            String.join(" ", labels));
        break;
      }
    }
  }

  static void composeFullChildrenHelp(CommandSender sender,
                                      Collection<Command> commands,
                                      String parent,
                                      String node) {
    composeFullChildrenHelp(sender, commands, parent, node, "");
  }

  static void composeFullChildrenHelp(CommandSender sender,
                                      Collection<Command> commands,
                                      String parent,
                                      String node,
                                      String extraMessage) {
    composeCategory(sender,
        commands,
        parent,
        node,
        extraMessage,
        false);
  }

  static void composeCategory(CommandSender sender,
                              Collection<Command> commands) {
    composeCategory(sender,
        commands,
        "margaret",
        "",
        "",
        true);
  }

  static void composeCategory(CommandSender sender,
                              Collection<Command> commands,
                              String parent,
                              String node,
                              String extraMessage,
                              boolean showParent) {
    String capitalize = StringUtils
        .capitalize(node.isEmpty()
            ? "margaret"
            : node);
    MessageUtils.compose(sender,
        Messages.COMMAND_LIST_PAGE_HEADER,
        false,
        capitalize);
    for (Command command : commands) {
      TextComponent nodeComponent =
          composeParentNode(parent, node, showParent);
      TextComponent commandComponent =
          TextComponent.of(command.getName());
      Component description = getTranslatedDescription(command);
      MessageUtils.composeInteractive(sender,
          Messages.COMMAND_LIST_ENTRY,
          false,
          nodeComponent,
          commandComponent,
          description);
    }
    if (!CommonsUtils.isNullOrEmpty(extraMessage)) {
      MessageUtils.compose(sender, extraMessage, false);
    }
    MessageUtils.compose(sender,
        Messages.COMMAND_LIST_PAGE_FOOTER,
        false);
  }

  static TextComponent composeParentNode(String parent,
                                         String node,
                                         boolean showParent) {
    TextComponent.Builder builder = showParent
        ? TextComponent.builder(parent)
        : MessageUtils.messageBuilder(Messages.HIDDEN_NODE)
        .hoverEvent(
            MessageUtils.hoverTextOf(
                Messages.HIDDEN_NODE_DESC,
                parent));
    if (!node.isEmpty()) {
      builder
          .append(" ")
          .append(node);
    }
    return builder.build();
  }

  static Component getTranslatedDescription(Command command) {
    Component description = command.getDescription();
    PluginCore core = MargaretMain.getCore();
    CommandFlow commandManager = core.getCommandManager();
    Translator translator = commandManager.getTranslator();
    return translator.translate(
        description,
        // Namespace never used in MargaretTranslator.
        null);
  }

  static Player toPlayer(Namespace namespace) {
    CommandSender sender = toSender(namespace);
    return sender instanceof Player
        ? (Player) sender
        : null;
  }

  static MargaretPlayer toMargaretPlayer(Namespace namespace) {
    Player player = toPlayer(namespace);
    return player != null
        ? MargaretPlayers.getAsMargaretPlayer(player)
        : EmptyObjects.getEmptyMargaretPlayer();
  }

  static Set<Command> getSubCommandsOf(@NotNull Command command) {
    return getSubCommandsOf(command.getName());
  }

  static Set<Command> getSubCommandsOf(String name) {
    PluginCore core = MargaretMain.getCore();
    CommandFlow commandManager = core.getCommandManager();
    return commandManager.getSubCommandsOf(name);
  }

  static Map<String, Set<Command>> getDeepSubCommandsOf(@NotNull Command command) {
    return getSubCommandOfPart(
        command.getName(),
        new HashMap<>(),
        command.getPart());
  }

  static Map<String, Set<Command>> getSubCommandOfPart(String commandName,
                                                       Map<String, Set<Command>> map,
                                                       CommandPart part) {
    if (part instanceof SubCommandPart) {
      SubCommandPart subCommandPart = (SubCommandPart) part;
      for (Command subCommand : subCommandPart.getSubCommands()) {
        map.compute(commandName,
            (k, v) -> {
              if (v == null)  {
                v = new TreeSet<>(
                    Comparator.comparing(Command::getName));
              }
              v.add(subCommand);
              return v;
            });
        map.putAll(getDeepSubCommandsOf(subCommand));
      }
    } else if (part instanceof PartsWrapper) {
      for (CommandPart commandPart : ((PartsWrapper) part).getParts()) {
        getSubCommandOfPart(commandName, map, commandPart);
      }
    } else if (part instanceof SinglePartWrapper) {
      getSubCommandOfPart(
          commandName,
          map,
          ((SinglePartWrapper) part).getPart());
    }

    return map;
  }
}
