package me.oczi.bukkit;

import me.oczi.bukkit.other.exceptions.PluginInitializationException;
import me.oczi.bukkit.utils.MessageUtils;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public final class MargaretMain extends JavaPlugin {
  private static Plugin plugin;
  private static MargaretCore core;

  private static boolean loadFailed;

  public MargaretMain() {
    plugin = this;
    core = new MargaretCore(this);
  }

  @Override
  public void onLoad() {
    try {
      core.preInitPlugin();
    } catch (IllegalAccessException |
        InvocationTargetException |
        IOException e) {
      e.printStackTrace();
      loadFailed = true;
    }
  }

  @Override
  public void onEnable() {
    if (loadFailed) {
      disableByFail("onLoad()");
    }

    try {
      core.initPlugin();
      MessageUtils.console("Plugin enabled successfully.", true);
    } catch (PluginInitializationException e) {
      e.printStackTrace();
      loadFailed = true;
      disableByFail("onEnable()");
    }
  }

  public void disableByFail(String phase) {
    getLogger().warning("The plugin has failed in " + phase + " phase.");
    getLogger().warning("Disabling plugin...");
    getPluginLoader().disablePlugin(this);
  }

  @Override
  public void onDisable() {
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

  public static PluginCore getCore() {
    return core;
  }

  public static Plugin getPlugin() {
    return plugin;
  }
}
