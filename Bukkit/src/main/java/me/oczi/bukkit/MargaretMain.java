package me.oczi.bukkit;

import me.oczi.bukkit.other.exceptions.CheckUpdateException;
import me.oczi.bukkit.other.exceptions.PluginInitializationException;
import me.oczi.bukkit.storage.yaml.MargaretYamlStorage;
import me.oczi.bukkit.utils.MessageUtils;
import me.oczi.bukkit.utils.update.MargaretVersionChecker;
import me.oczi.common.utils.CommonsUtils;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public final class MargaretMain extends JavaPlugin {
  private static Plugin plugin;
  private static MargaretCore core;
  private static MargaretVersionChecker checker;

  private static boolean loadFailed;
  private static boolean isCompatible;

  public MargaretMain() {
    plugin = this;
    core = new MargaretCore(this);
  }

  @Override
  public void onLoad() {
    isCompatible = CommonsUtils.isClassLoaded("com.google.gson.Gson");
    if (isCompatible) {
      try {
        core.preInitPlugin();
      } catch (IllegalAccessException |
          InvocationTargetException |
          IOException e) {
        e.printStackTrace();
        loadFailed = true;
      }
    }
  }

  @Override
  public void onEnable() {
    if (!isCompatible) {
      disableByIncompatibility();
      return;
    }
    if (loadFailed) {
      disableByFail("onLoad()");
      return;
    }

    try {
      core.initPlugin();
      MessageUtils.console("Plugin enabled successfully.", true);
    } catch (PluginInitializationException e) {
      e.printStackTrace();
      loadFailed = true;
      disableByFail("onEnable()");
    }

    if (!MargaretYamlStorage.isUpdateCheck()) {
      MessageUtils.console("Update checker disabled.", true);
      return;
    }
    checker = MargaretVersionChecker.newChecker(getVersion());
    try {
      checker.checkUpdate();
      if (checker.hasUpdate()) {
        MessageUtils.console("** IMPORTANT **", true);
        MessageUtils.console("A new version is available! ({0})", true,
            checker.getRelease().getVersion());
        MessageUtils.console("Github URL: {0}", true,
            // Hardcoded /releases for github
            getDescription().getWebsite() + "/releases");
      } else {
        MessageUtils.console("No new versions found.", true);
      }
    } catch (CheckUpdateException e) {
      e.printStackTrace();
    }
  }

  public void disableByIncompatibility() {
    getLogger().warning("Server doesn't support Google's gson library.");
    getLogger().warning("this will probably due to a outdated version of Spigot or derived.");
    getLogger().warning("Please, use Spigot 1.8.8 or superior for this plugin.");
    getPluginLoader().disablePlugin(this);
  }

  public void disableByFail(String phase) {
    getLogger().warning("The plugin has failed in " + phase + " phase.");
    getLogger().warning("Disabling plugin...");
    getPluginLoader().disablePlugin(this);
  }

  @Override
  public void onDisable() {
    if (!isCompatible ||
        !CommonsUtils.isClassLoaded("net.kyori.text")) {
      getLogger().info("Plugin disabled.");
      return;
    }
    if (!loadFailed) {
      core.disablePlugin();
    }
    MessageUtils.console("Plugin disabled.", true);
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  public File createAndGetFile(String filename)
      throws IOException {
    File file = new File(getDataFolder(), filename);
    file.mkdirs();
    if (!file.exists()) { file.createNewFile(); }
    return file;
  }

  public static String getVersion() {
    return plugin.getDescription().getVersion();
  }

  public static MargaretVersionChecker getUpdateChecker() {
    return checker;
  }

  public static PluginCore getCore() {
    return core;
  }

  public static Plugin getPlugin() {
    return plugin;
  }
}
