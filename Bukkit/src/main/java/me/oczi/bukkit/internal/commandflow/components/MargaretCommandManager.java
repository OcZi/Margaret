package me.oczi.bukkit.internal.commandflow.components;

import me.fixeddev.commandflow.bukkit.BukkitCommandManager;
import me.fixeddev.commandflow.bukkit.BukkitCommandWrapper;
import me.fixeddev.commandflow.command.Command;

public class MargaretCommandManager extends BukkitCommandManager {

  public MargaretCommandManager(String fallbackPrefix) {
    super(fallbackPrefix);
  }

  @Override
  public void registerCommand(Command command) {
    manager.registerCommand(command);

    BukkitCommandWrapper bukkitCommand = new MargaretCommandWrapper(command,
        this, getTranslator());

    wrapperMap.put(command.getName(), bukkitCommand);
    bukkitCommandMap.register(fallbackPrefix, bukkitCommand);
  }
}
