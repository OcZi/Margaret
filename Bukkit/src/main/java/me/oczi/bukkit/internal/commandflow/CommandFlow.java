package me.oczi.bukkit.internal.commandflow;

import me.fixeddev.commandflow.CommandManager;
import me.fixeddev.commandflow.command.Command;
import me.fixeddev.commandflow.translator.Translator;

import java.util.Map;
import java.util.Set;

public interface CommandFlow {

  void init();

  Translator getTranslator();

  CommandManager getCommandManager();

  Set<Command> getSubCommandsOf(String commandName);

  Map<String, Set<Command>> getMapCommand();
}
