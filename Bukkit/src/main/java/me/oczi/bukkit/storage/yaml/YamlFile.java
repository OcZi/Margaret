package me.oczi.bukkit.storage.yaml;

import me.oczi.common.utils.CommonsUtils;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Basic YamlConfiguration file representation.
 */
public class YamlFile {
  private final JavaPlugin plugin;
  private final Map<String, String> mapRefill;

  private final FileConfiguration fileConfig;
  private String fileName;
  private final File file;

  public YamlFile(JavaPlugin plugin,
                  String fileName) {
    this(plugin, fileName, null);
  }

  public YamlFile(JavaPlugin plugin,
                  String fileName,
                  @Nullable Map<String, String> mapRefill) {
    this(plugin,
        new File(plugin.getDataFolder(), fileName),
        mapRefill);
  }

  public YamlFile(JavaPlugin plugin,
                  File file) {
    this(plugin, file, null);
  }

  public YamlFile(JavaPlugin plugin,
                  File file,
                  @Nullable Map<String, String> mapRefill) {
    this.plugin = plugin;
    this.mapRefill = mapRefill;
    this.file = file;
    this.fileConfig = new YamlConfiguration();
    this.fileName = file.getName();
    saveDefault();
    loadFileConfiguration();

    if (mapRefill != null)  { refillNodes(); }
  }

  public void loadFileConfiguration() {
    try {
      if (CommonsUtils.isNullOrEmpty(fileName))
        throw new NullPointerException(
            "File name is empty or null.");
      if (!fileName.endsWith(".yml")) {
        fileName += ".yml";
      }

      fileConfig.load(file);
    } catch (IOException | InvalidConfigurationException e) {
      e.printStackTrace();
    }
  }

  public void refillNodes() {
    for (Map.Entry<String, String> mapEntry : mapRefill.entrySet()) {
      if (!fileConfig.isSet(mapEntry.getKey())) {
        fileConfig.set(mapEntry.getKey(), mapEntry.getValue());
      }
    }
    save();
  }

  public void save() {
    try {
      fileConfig.save(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void saveDefault() {
    if (!file.exists()) {
      plugin.saveResource(fileName, false);
    }
  }

  public File getFile() {
    return file;
  }

  public String getFileName() {
    return fileName;
  }

  public FileConfiguration getAccess() {
    return fileConfig;
  }
}
