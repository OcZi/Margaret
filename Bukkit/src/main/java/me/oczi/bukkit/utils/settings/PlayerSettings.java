package me.oczi.bukkit.utils.settings;

import me.oczi.common.exceptions.NotInstantiatedClassException;

import java.util.HashMap;
import java.util.Map;

/**
 * Player settings utilities.
 */
public final class PlayerSettings {
  private static final Map<String, EnumSettings> basicSettings = new HashMap<>();
  private static final Map<String, EnumSettings> cacheSettings = new HashMap<>();
  private static final Map<String, EnumSettings> partnerSettings = new HashMap<>();

  private static final Map<String, EnumSettings> allSettings = new HashMap<>();
  private static final Map<String, EnumSettings> databaseSettings = new HashMap<>();

  private PlayerSettings() {
    throw new NotInstantiatedClassException();
  }

  public static Map<String, EnumSettings> getBasicSettings() {
    if (allSettings.isEmpty()) { generateDefaultSettings(); }
    return basicSettings;
  }

  public static Map<String, EnumSettings> getCacheSettings() {
    if (allSettings.isEmpty()) { generateDefaultSettings(); }
    return cacheSettings;
  }

  public static Map<String, EnumSettings> getPartnerSettings() {
    if (allSettings.isEmpty()) { generateDefaultSettings(); }
    return partnerSettings;
  }

  public static Map<String, EnumSettings> getAllSettings() {
    if (allSettings.isEmpty()) { generateDefaultSettings(); }
    return allSettings;
  }

  public static Map<String, EnumSettings> getDatabaseSettings() {
    if (allSettings.isEmpty()) { generateDefaultSettings(); }
    return databaseSettings;
  }

  private static void generateDefaultSettings() {
    for (BasicSettings value : BasicSettings.values()) {
      basicSettings.put(value.getName(), value);
    }

    for (PartnerSettings value : PartnerSettings.values()) {
      partnerSettings.put(value.getName(), value);
    }

    for (CacheSettings value : CacheSettings.values()) {
      cacheSettings.put(value.getName(), value);
    }

    allSettings.putAll(basicSettings);
    allSettings.putAll(partnerSettings);
    databaseSettings.putAll(allSettings);

    allSettings.putAll(cacheSettings);
  }
}
