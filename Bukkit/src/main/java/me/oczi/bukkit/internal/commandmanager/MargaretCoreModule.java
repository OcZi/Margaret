package me.oczi.bukkit.internal.commandmanager;

import app.ashcon.intake.parametric.AbstractModule;
import me.oczi.bukkit.MargaretMain;
import me.oczi.bukkit.PluginCore;
import me.oczi.bukkit.internal.CooldownManager;
import me.oczi.bukkit.internal.MemoryManager;
import me.oczi.bukkit.internal.commandmanager.providers.*;
import me.oczi.bukkit.internal.database.DatabaseManager;
import me.oczi.bukkit.internal.database.DbTasks;
import me.oczi.bukkit.objects.Gender;
import me.oczi.bukkit.objects.partnership.Partnership;
import me.oczi.bukkit.objects.player.MargaretPlayer;
import me.oczi.bukkit.utils.GenderManager;
import me.oczi.bukkit.utils.Genders;
import me.oczi.bukkit.utils.PartnershipPermission;
import me.oczi.bukkit.utils.settings.EnumSettings;
import org.bukkit.plugin.Plugin;

import java.util.logging.Logger;

public class MargaretCoreModule extends AbstractModule {
  private final CommandManager commandManager;
  private final PluginCore core;

  public MargaretCoreModule(CommandManager commandManager, PluginCore core) {
    this.commandManager = commandManager;
    this.core = core;
  }

  @Override
  protected void configure() {
    bind(Plugin.class).toInstance(MargaretMain.getPlugin());
    bind(PluginCore.class).toInstance(core);
    bind(CooldownManager.class).toInstance(core.getCooldownManager());
    bind(MemoryManager.class).toInstance(core.getMemoryManager());
    bind(CommandManager.class).toInstance(commandManager);
    bind(GenderManager.class).toInstance(Genders.getGenderManager());
    bind(DatabaseManager.class).toInstance(core.getDatabaseManager());
    bind(DbTasks.class).toInstance(core.getDatabaseTask());
    bind(MargaretPlayer.class).toProvider(new MargaretPlayerProvider());
    bind(Partnership.class).toProvider(new PartnershipProvider());
    bind(Gender.class).toProvider(new GenderProvider());
    bind(Logger.class).toInstance(core.getLogger());
    bind(EnumSettings.class).toProvider(new SettingProvider());
    bind(PartnershipPermission.class).toProvider(new PartnershipPermissionProvider());
  }
}
