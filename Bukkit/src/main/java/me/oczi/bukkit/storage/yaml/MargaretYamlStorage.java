package me.oczi.bukkit.storage.yaml;

import me.oczi.bukkit.utils.MessageUtils;
import me.oczi.bukkit.utils.Messages;
import org.bukkit.Bukkit;
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
  private static int commandCooldown;
  private static int daysToExpire;

  private static int threads;

  private static int partnerMaxHomes;
  private static int maxPossibleHomes;
  private static int proposalCooldown;

  private static boolean debugMode;
  private static boolean announcePartner;
  private static boolean playerAuthentication;
  private static boolean validatePlayerAuthentication;
  private static boolean updateCheck;
  private static boolean offlineMode;

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
    threads = databaseConfig
        .getInt("general.threads", 1);
    databaseType = databaseConfig
        .getString("general.database-type", "sqlite");
  }

  private static void newMainConfigVar() {
    allowedRelations = mainConfig.getStringList("partnership.relation.allowed-relations");
    checkRelations();

    defaultPartnerPermissions = mainConfig.getStringList("partnership.permission.default-settings");

    maxPossibleHomes = mainConfig.getInt("partnership.max-possible-homes", 1);

    maxProposals = mainConfig.getInt("player.maximum-proposals", 10);
    proposalTimeout = mainConfig.getInt("player.proposal-time-out", 30);
    proposalCooldown = mainConfig.getInt("player.proposal-cooldown", 30);
    commandCooldown = mainConfig.getInt("command.command-cooldown", 30);
    daysToExpire = mainConfig.getInt("player.player-days-expire", 30);
    partnerMaxHomes = mainConfig.getInt("partnership.permission.default-max-homes", 5);

    debugMode = mainConfig.getBoolean("other.debug-mode", false);
    updateCheck = mainConfig.getBoolean("other.update-check", false);
    offlineMode = mainConfig.getBoolean("other.offline-mode", false);
    announcePartner = mainConfig.getBoolean("partnership.announce-partnership", false);
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

  public static boolean isPlayerAuthentication() {
    if (!validatePlayerAuthentication) {
      playerAuthentication = mainConfig
          .getBoolean("player.player-authentication", true);
      if (!Bukkit.getOnlineMode() && playerAuthentication) {
        MessageUtils.warning(
            "Cannot activate player authentication in offline mode.",
            "Set Player authentication to false.");
        playerAuthentication = false;
      }
      validatePlayerAuthentication = true;
    }
    return playerAuthentication;
  }

  public static boolean isUpdateCheck() {
    return updateCheck;
  }

  public static boolean isOfflineMode() {
    return offlineMode;
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

  public static int getCommandCooldown() {
    return commandCooldown;
  }

  public static int getProposalTimeout() {
    return proposalTimeout;
  }

  public static int getProposalCooldown() {
    return proposalCooldown;
  }

  public static int getMaxProposals() {
    return maxProposals;
  }

  public static int getDaysToExpire() {
    return daysToExpire;
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
