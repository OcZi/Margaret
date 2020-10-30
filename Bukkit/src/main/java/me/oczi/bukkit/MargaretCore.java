package me.oczi.bukkit;

import me.oczi.bukkit.internal.*;
import me.oczi.bukkit.internal.commandmanager.CommandManager;
import me.oczi.bukkit.internal.commandmanager.MargaretCommandManager;
import me.oczi.bukkit.internal.database.DatabaseManager;
import me.oczi.bukkit.internal.database.DbTasks;
import me.oczi.bukkit.internal.database.sql.SqlManagerImpl;
import me.oczi.bukkit.listeners.ChatListener;
import me.oczi.bukkit.listeners.PlayerListener;
import me.oczi.bukkit.objects.partnership.Partnership;
import me.oczi.bukkit.objects.player.MargaretPlayer;
import me.oczi.bukkit.other.exceptions.PluginInitializationException;
import me.oczi.bukkit.storage.yaml.MargaretYamlStorage;
import me.oczi.bukkit.storage.yaml.impl.CacheConfigImpl;
import me.oczi.bukkit.utils.GenderManager;
import me.oczi.bukkit.utils.Genders;
import me.oczi.bukkit.utils.MessageUtils;
import me.oczi.bukkit.utils.lib.LibraryLoader;
import me.oczi.bukkit.utils.lib.MargaretLibrary;
import me.oczi.common.api.configuration.CacheConfig;
import me.oczi.common.dependency.Dependency;
import me.oczi.common.dependency.DependencyManager;
import me.oczi.common.storage.sql.datasource.DataSourceType;
import me.oczi.common.utils.CommonsUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import static me.oczi.common.storage.sql.datasource.DataSourceType.MYSQL;
import static me.oczi.common.storage.sql.datasource.DataSourceType.SQLITE;

/**
 * Margaret's core.
 * Initialize all the components in order and
 * expose them in getters.
 */
public class MargaretCore implements PluginCore {
  private final MargaretMain plugin;

  private CooldownManager cooldownManager;
  private ObjectCycleManager objectCycleManager;

  private DatabaseManager dbManager;
  private MemoryManager memoryManager;
  private GenderManager genderManager;
  private CommandManager commandManager;
  private DependencyManager libraryLoader;
  private List<Dependency> loadedDependencies;

  MargaretCore(MargaretMain plugin) {
    this.plugin = plugin;
  }

  void preInitPlugin()
      throws IllegalAccessException,
      IOException,
      InvocationTargetException {
    initYamlStorage();
    initDatabase();
    initLibraries();
  }

  void initPlugin()
      throws PluginInitializationException {
    try {
      dbManager.init();
      dbManager.scriptInit();
      initGenders();
      initObjectManager();
      initListeners();
      initCooldown();
      initCommand();
      initFinished();
    } catch (Exception e) {
      throw new PluginInitializationException("Plugin initialization failed.", e);
    }
  }

  private void initYamlStorage() {
    loadYamlStorage();
  }

  private void initLibraries()
      throws IllegalAccessException,
      IOException,
      InvocationTargetException {
    ClassLoader classLoader = getClass().getClassLoader();
    Logger logger = getLogger();
    this.libraryLoader = new LibraryLoader(
        getLibFolder(), classLoader, logger);

    libraryLoader
        .addDependency(MargaretLibrary.getDefaultDependencies());
    DataSourceType dataSourceType = dbManager.getDatabaseType();
    if (!CommonsUtils.equalsTo(
        dataSourceType, SQLITE, MYSQL)) {
      String databaseName = dbManager
          .getDatabaseTypeName().toUpperCase();
      MargaretLibrary dbLibrary = MargaretLibrary
          .valueOf(databaseName);
      libraryLoader.addDependency(dbLibrary);
    }
    loadedDependencies = libraryLoader.process();
  }

  private void initObjectManager() {
    CacheConfig config = new CacheConfigImpl(
        MargaretYamlStorage.getMainConfig());
    this.memoryManager = new MemoryManagerImpl(config);
    this.objectCycleManager = new ObjectCycleManagerImpl(memoryManager);
  }

  private void initDatabase() {
    // TODO: Change SqlManager to DatabaseManager if MongoDBManager is implemented
    this.dbManager = new SqlManagerImpl(
        MargaretYamlStorage.isDebugMode());
  }

  private void initListeners() {
    Bukkit.getPluginManager()
        .registerEvents(new PlayerListener(), plugin);

    Bukkit.getPluginManager()
        .registerEvents(new ChatListener(), plugin);
  }

  private void initCooldown() {
    int cooldownEviction = MargaretYamlStorage.getCommandTimeout();
    int proposalTimeout = MargaretYamlStorage.getProposalTimeout();
    this.cooldownManager = new CooldownManagerImpl(
        cooldownEviction, proposalTimeout);
  }

  private void initGenders() {
    // Load lazy initialization
    this.genderManager = new GenderManagerImpl(
            MargaretYamlStorage.getGendersConfig());
  }

  private void initCommand() {
    this.commandManager = new MargaretCommandManager(this, plugin);
  }

  private void initFinished() {
    // Hardcoded messages
    // This will never be necessary to translate, right?
    MessageUtils.console
        ("Database type: "
            + dbManager.getDatabaseTypeName(), true);
    MessageUtils.console
        ("Threads used: "
            + dbManager.getThreadsUsed(), true);
    MessageUtils.console
        ("Genders registered: " +
            Genders.getGendersNameColorized(), true);
  }

  public void loadYamlStorage() {
    MargaretYamlStorage.generateYamlFiles(plugin);
  }

  public void callEvent(Event event) {
    Bukkit.getPluginManager().callEvent(event);
  }

  void disablePlugin() {
    dbManager.shutdown();
    plugin.getPluginLoader().disablePlugin(plugin);
  }

  @Override
  public Partnership getPartner(String id) {
    return memoryManager.getPersistenceCache().getPartner(id);
  }

  @Override
  public MargaretPlayer getMargaretPlayer(UUID uuid) {
    return memoryManager.getPersistenceCache().getMargaretPlayer(uuid);
  }

  @Override
  public Logger getLogger() {
    return plugin.getLogger();
  }

  @Override
  public File createAndGetFile(String filename) {
    try {
      return plugin.createAndGetFile(filename);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }

  @Override
  public DatabaseManager getDatabaseManager() {
    return dbManager;
  }

  @Override
  public DbTasks getDatabaseTask() {
    return dbManager.getDatabaseTask();
  }

  @Override
  public CooldownManager getCooldownManager() {
    return cooldownManager;
  }

  @Override
  public MemoryManager getMemoryManager() {
    return memoryManager;
  }

  @Override
  public File getDataFolder() {
    return plugin.getDataFolder();
  }

  @Override
  public File getLibFolder() {
    return new File(plugin.getDataFolder() + "/lib/");
  }

  @Override
  public List<Dependency> getLoadedDependencies() {
    return loadedDependencies;
  }

  @Override
  public CommandManager getCommandManager() {
    return commandManager;
  }

  @Override
  public GenderManager getGenderManager() {
    return genderManager;
  }

  @Override
  public ObjectCycleManager getObjectCycleManager() {
    return objectCycleManager;
  }
}
