package me.oczi.bukkit.internal.commandflow;

import me.fixeddev.commandflow.CommandManager;
import me.fixeddev.commandflow.annotated.AnnotatedCommandTreeBuilder;
import me.fixeddev.commandflow.annotated.part.PartInjector;
import me.fixeddev.commandflow.command.Command;
import me.fixeddev.commandflow.translator.Translator;
import me.oczi.bukkit.PluginCore;
import me.oczi.bukkit.commands.CommandMain;
import me.oczi.bukkit.internal.commandflow.components.MargaretCommandManager;
import me.oczi.bukkit.internal.commandflow.components.MargaretModule;
import me.oczi.bukkit.internal.commandflow.components.MargaretTranslator;
import me.oczi.bukkit.internal.commandflow.components.MargaretUsageBuilder;
import me.oczi.bukkit.internal.commandflow.parts.SingletonPart;
import me.oczi.bukkit.utils.Commands;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

public class MargaretCommandFlow implements CommandFlow {
  private Map<String, Set<Command>> mapCommand;

  private final CommandManager commandManager;
  private final PartInjector injector;
  private final Translator translator;

  public MargaretCommandFlow(PluginCore core,
                             JavaPlugin plugin) {
    this(core, plugin.getName());
  }

  public MargaretCommandFlow(PluginCore core, String name) {
    this.translator = new MargaretTranslator();
    this.commandManager = new MargaretCommandManager(name);
    this.commandManager.setUsageBuilder(new MargaretUsageBuilder());
    this.commandManager.setTranslator(translator);
    this.injector = MargaretModule.newInjector(core);
  }

  @Override
  public void init() {
    // Bind himself
    this.injector.bindPart(
        CommandFlow.class,
        SingletonPart.of(
            this, getClass().getSimpleName()));
    AnnotatedCommandTreeBuilder builder = AnnotatedCommandTreeBuilder
        .create(injector);
    this.commandManager.registerCommands(
        builder.fromClass(new CommandMain()));
    Command margaretCommand = commandManager
        .getCommand("margaret").orElse(null);
    this.mapCommand = Commands.getDeepSubCommandsOf(
        checkNotNull(margaretCommand,
            "Main command is null (Bad initialization?)"));
  }

  @Override
  public Translator getTranslator() {
    return translator;
  }

  @Override
  public CommandManager getCommandManager() {
    return commandManager;
  }

  @Override
  public Set<Command> getSubCommandsOf(String commandName) {
    return mapCommand.get(commandName.toLowerCase());
  }

  @Override
  public Map<String, Set<Command>> getMapCommand() {
    return mapCommand;
  }
}
