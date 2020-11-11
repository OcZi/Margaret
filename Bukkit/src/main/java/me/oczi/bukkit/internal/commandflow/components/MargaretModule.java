package me.oczi.bukkit.internal.commandflow.components;

import me.fixeddev.commandflow.annotated.part.PartInjector;
import me.fixeddev.commandflow.annotated.part.defaults.DefaultsModule;
import me.fixeddev.commandflow.bukkit.factory.BukkitModule;
import me.oczi.bukkit.MargaretMain;
import me.oczi.bukkit.PluginCore;
import me.oczi.bukkit.internal.CooldownManager;
import me.oczi.bukkit.internal.MemoryManager;
import me.oczi.bukkit.internal.commandflow.factory.*;
import me.oczi.bukkit.internal.commandflow.parts.SingletonPart;
import me.oczi.bukkit.internal.database.DatabaseManager;
import me.oczi.bukkit.internal.database.DbTasks;
import me.oczi.bukkit.objects.Gender;
import me.oczi.bukkit.objects.partnership.Partnership;
import me.oczi.bukkit.objects.player.MargaretPlayer;
import me.oczi.bukkit.utils.GenderManager;
import me.oczi.bukkit.utils.Keys;
import me.oczi.bukkit.utils.PartnershipPermission;
import me.oczi.bukkit.utils.settings.EnumSetting;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

public class MargaretModule extends BukkitModule {
  private final PluginCore core;

  public MargaretModule(PluginCore core) {
    this.core = core;
  }

  public static PartInjector newInjector(PluginCore core) {
    PartInjector partInjector = PartInjector.create();
    partInjector.install(new MargaretModule(core));
    partInjector.install(new DefaultsModule());
    return partInjector;
  }

  @Override
  public void configure() {
    super.configure();
    // Argument injection
    bindFactory(Location.class, new LocationFactory());
    bindFactory(MargaretPlayer.class, new MargaretPlayerFactory());
    bindFactory(Partnership.class,  new PartnershipFactory());
    bindFactory(Gender.class, new GenderFactory());
    bindFactory(EnumSetting.class, new EnumSettingFactory());
    bindFactory(
        PartnershipPermission.class,
        new PartnershipPermissionFactory());
    bindFactory(
        Keys.senderOf(Location.class),
        new LocationSenderFactory());
    bindFactory(
        Keys.senderOf(MargaretPlayer.class),
        new MargaretSenderFactory());
    bindFactory(
        Keys.senderOf(Partnership.class),
        new PartnershipSenderFactory());

    // Component injection
    bind(Plugin.class, MargaretMain.getPlugin());
    bind(PluginCore.class, core);
    bind(CooldownManager.class, core.getCooldownManager());
    bind(MemoryManager.class, core.getMemoryManager());
    bind(GenderManager.class, core.getGenderManager());
    bind(DatabaseManager.class, core.getDatabaseManager());
    bind(DbTasks.class, core.getDatabaseTask());
  }

  public <T> void bind(Class<T> clazz, T object) {
    bind(clazz,
        // Wrap object into a CommandPart
        SingletonPart.of(
            object, clazz.getSimpleName()));
  }
}
