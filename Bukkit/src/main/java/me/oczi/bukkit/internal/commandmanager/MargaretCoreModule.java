package me.oczi.bukkit.internal.commandmanager;

import app.ashcon.intake.parametric.AbstractModule;
import me.oczi.bukkit.MargaretMain;
import me.oczi.bukkit.PluginCore;
import me.oczi.bukkit.internal.CooldownManager;
import me.oczi.bukkit.internal.commandmanager.providers.*;
import me.oczi.bukkit.internal.database.DatabaseManager;
import me.oczi.bukkit.internal.database.DbTasks;
import me.oczi.bukkit.objects.CooldownPlayer;
import me.oczi.bukkit.objects.Gender;
import me.oczi.bukkit.objects.partner.Partner;
import me.oczi.bukkit.objects.player.MargaretPlayer;
import me.oczi.bukkit.utils.GenderManager;
import me.oczi.bukkit.utils.Genders;
import me.oczi.bukkit.utils.PartnerPermission;
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
    bind(PluginCore.class).toInstance(core);
    bind(Plugin.class).toInstance(MargaretMain.getPlugin());
    bind(CommandManager.class).toInstance(commandManager);
    bind(DatabaseManager.class).toInstance(core.getDatabaseManager());
    bind(DbTasks.class).toInstance(core.getDatabaseTask());
    bind(GenderManager.class).toInstance(Genders.getGenderManager());
    bind(Gender.class).toProvider(new GenderProvider());
    bind(Logger.class).toInstance(core.getLogger());
    bind(CooldownManager.class).toInstance(core.getCooldownManager());
    bind(MargaretPlayer.class).toProvider(new MargaretPlayerProvider());
    bind(EnumSettings.class).toProvider(new SettingProvider());
    bind(Partner.class).toProvider(new PartnerProvider());
    bind(PartnerPermission.class).toProvider(new PartnerPermissionProvider());
  }
}
