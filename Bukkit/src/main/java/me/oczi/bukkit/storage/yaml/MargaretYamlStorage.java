package me.oczi.bukkit.storage.yaml;

import me.oczi.bukkit.utils.MessageUtils;
import me.oczi.bukkit.utils.Messages;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Storage of all {@link YamlFile}s of plugin
 * and their list of values.
 */
public final class MargaretYamlStorage {
  private static JavaPlugin plugin;

  private static YamlFile mainConfig;
  private static YamlFile messageConfig;
  private static YamlFile messageColorsConfig;
  private static YamlFile gendersConfig;
  private static YamlFile databaseConfig;

  private static List<String> allowedRelations;
  private static List<String> defaultPartnerPermissions;

  private static String databaseType;

  private static int maxProposals;
  private static int proposalTimeout;
  private static int commandTimeout;
  private static int daysToExpire;

  private static int threads;

  private static int cacheTimeOut;
  private static int partnerMaxHomes;
  private static int maxPossibleHomes;

  private static boolean debugMode;
  private static boolean announcePartner;

  public static void generateYamlFiles(JavaPlugin plugin) {
    MargaretYamlStorage.plugin = plugin;
    newMainConfig();
    newMessageConfig();
    newMessageColors();
    newGendersConfig();
    newDatabaseConfig();
  }


  private static void newMainConfig() {
    mainConfig = new YamlFile(plugin,
        "config.yml");
    newMainConfigVar();
  }

  private static void newMessageConfig() {
    messageConfig = new YamlFile(
        plugin,
        "messages.yml",
        allMessagesToMap());
  }

  private static void newMessageColors() {
    messageColorsConfig = new YamlFile(
        plugin,
        "messages-colors.yml");
  }

  private static void newGendersConfig() {
    gendersConfig = new YamlFile(plugin, "genders.yml");
  }

  private static void newDatabaseConfig() {
    databaseConfig = new YamlFile(plugin, "database.yml");
    threads = databaseConfig.getAccess()
        .getInt("general.threads", 1);
    databaseType = databaseConfig.getAccess()
        .getString("general.database-type", "sqlite");
  }

  private static void newMainConfigVar() {
    FileConfiguration access = mainConfig.getAccess();
    allowedRelations = access.getStringList("partner.relation.allowed-relations");
    checkRelations();

    defaultPartnerPermissions = access.getStringList("partner.permission.default-settings");

    maxPossibleHomes = access.getInt("partner.max-possible-homes", 1);

    maxProposals = access.getInt("player.maximum-proposals", 10);
    proposalTimeout = access.getInt("player.proposal-time-out", 30);
    commandTimeout = access.getInt("command.command-cooldown", 30);
    daysToExpire = access.getInt("player.player-expire", 30);
    partnerMaxHomes = access.getInt("partner.permission.default-max-homes", 5);

    debugMode = access.getBoolean("other.debug-mode", false);
    announcePartner = access.getBoolean("partner.announce-partner", false);
  }

  /**
   * Get all messages of the plugin.
   * Used for refill Messages.yml configuration.
   * @return Map of all messages.
   */
  public static Map<String, String> allMessagesToMap() {
    Map<String, String> messageMap = new LinkedHashMap<>();
    for (Messages message : Messages.defaultMessagesSet()) {
      messageMap.put(message.getNode(), message.getMessage());
    }

    return messageMap;
  }

  private static void checkRelations() {
    for (String allowedRelation : allowedRelations) {
      if (allowedRelation.length() > 16) {
        MessageUtils.warning(allowedRelation + " reached the limit of characters.");
        allowedRelations.remove(allowedRelation);
      }
    }
    // Always register unknown.
    if (!allowedRelations.contains("unknown")) {
      allowedRelations.add("unknown");
    }
  }

  public static void reloadMessages() {
    newMessageConfig();
  }

  public static void reloadMessageColors() {
    newMessageColors();
  }

  public static void reloadMainConfig() {
    newMainConfig();
  }

  public static boolean isDebugMode() {
    return debugMode;
  }

  public static boolean isAnnouncePartner() {
    return announcePartner;
  }

  public static List<String> getAllowedRelations() {
    return allowedRelations;
  }

  public static int getMaxPossibleHomes() {
    return maxPossibleHomes;
  }

  public static YamlFile getMainConfig() {
    return mainConfig;
  }

  public static YamlFile getMessageConfig() {
    return messageConfig;
  }

  public static YamlFile getMessageColorsConfig() {
    return messageColorsConfig;
  }

  public static YamlFile getGendersConfig() {
    return gendersConfig;
  }

  public static YamlFile getDatabaseConfig() {
    return databaseConfig;
  }

  public static int getCommandTimeout() {
    return commandTimeout;
  }

  public static int getProposalTimeout() {
    return proposalTimeout;
  }

  public static int getMaxProposals() {
    return maxProposals;
  }

  public static int getDaysToExpire() {
    return daysToExpire;
  }

  public static int getCacheTimeOut() {
    return cacheTimeOut;
  }

  public static int getPartnerMaxHomes() {
    return partnerMaxHomes;
  }

  public static String getDatabaseType() {
    return databaseType;
  }

  public static int getThreads() {
    return threads;
  }

  public static List<String> getDefaultPartnerPermissions() {
    return defaultPartnerPermissions;
  }

  public static String getDefaultRelation() {
    return allowedRelations.get(0);
  }
}
