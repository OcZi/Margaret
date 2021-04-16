package me.oczi.bukkit.utils.settings;

import me.oczi.common.exceptions.NotInstantiatedClassException;

import java.util.HashMap;
import java.util.Map;

/**
 * Player settings utilities.
 */
public final class PlayerSettings {
  private static final Map<String, EnumSetting> basicSettings = new HashMap<>();
  private static final Map<String, EnumSetting> cacheSettings = new HashMap<>();
  private static final Map<String, EnumSetting> partnerSettings = new HashMap<>();

  private static final Map<String, EnumSetting> allSettings = new HashMap<>();
  private static final Map<String, EnumSetting> databaseSettings = new HashMap<>();

  private PlayerSettings() {
    throw new NotInstantiatedClassException();
  }

  public static Map<String, EnumSetting> getBasicSettings() {
    if (allSettings.isEmpty()) { generateDefaultSettings(); }
    return basicSettings;
  }

  public static Map<String, EnumSetting> getCacheSettings() {
    if (allSettings.isEmpty()) { generateDefaultSettings(); }
    return cacheSettings;
  }

  public static Map<String, EnumSetting> getPartnerSettings() {
    if (allSettings.isEmpty()) { generateDefaultSettings(); }
    return partnerSettings;
  }

  public static Map<String, EnumSetting> getAllSettings() {
    if (allSettings.isEmpty()) { generateDefaultSettings(); }
    return allSettings;
  }

  public static Map<String, EnumSetting> getDatabaseSettings() {
    if (allSettings.isEmpty()) { generateDefaultSettings(); }
    return databaseSettings;
  }

  private static void generateDefaultSettings() {
    for (BasicSetting value : BasicSetting.values()) {
      basicSettings.put(value.getName(), value);
    }

    for (PartnershipSetting value : PartnershipSetting.values()) {
      partnerSettings.put(value.getName(), value);
    }

    for (CacheSetting value : CacheSetting.values()) {
      cacheSettings.put(value.getName(), value);
    }

    allSettings.putAll(basicSettings);
    allSettings.putAll(partnerSettings);
    databaseSettings.putAll(allSettings);

    allSettings.putAll(cacheSettings);
  }

  public static EnumSetting getSetting(String name) {
    name = name.toLowerCase()
        .replace("-", "_");
    return allSettings.get(name);
  }
}
