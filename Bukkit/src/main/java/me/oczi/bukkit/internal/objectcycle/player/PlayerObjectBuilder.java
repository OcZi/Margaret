package me.oczi.bukkit.internal.objectcycle.player;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.collect.Maps;
import me.oczi.bukkit.PluginCore;
import me.oczi.bukkit.internal.database.DbTasks;
import me.oczi.bukkit.objects.Proposal;
import me.oczi.bukkit.objects.collections.CacheSet;
import me.oczi.bukkit.objects.collections.CacheSetImpl;
import me.oczi.bukkit.objects.partnership.Partnership;
import me.oczi.bukkit.objects.player.MargaretPlayer;
import me.oczi.bukkit.objects.player.MargaretPlayerImpl;
import me.oczi.bukkit.objects.player.MargaretPlayerMeta;
import me.oczi.bukkit.objects.player.PlayerData;
import me.oczi.bukkit.other.ProposalWriter;
import me.oczi.bukkit.other.exceptions.PlayerVerificationException;
import me.oczi.bukkit.storage.yaml.MargaretYamlStorage;
import me.oczi.bukkit.utils.DefaultGender;
import me.oczi.bukkit.utils.EmptyObjects;
import me.oczi.bukkit.utils.MessageUtils;
import me.oczi.bukkit.utils.Partnerships;
import me.oczi.bukkit.utils.settings.EnumSettings;
import me.oczi.bukkit.utils.settings.PlayerSettings;
import me.oczi.common.api.mojang.MojangAccount;
import me.oczi.common.exceptions.SQLCastException;
import me.oczi.common.request.mojang.AsyncMojangResolver;
import me.oczi.common.request.mojang.MojangResolver;
import me.oczi.common.storage.sql.dsl.result.SqlObject;
import me.oczi.common.utils.CommonsUtils;
import me.oczi.common.utils.Statements;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public class PlayerObjectBuilder {
  private final PluginCore core;

  private final DbTasks dbTasks;

  private final int maxProposals = MargaretYamlStorage.getMaxProposals();
  private final int daysToExpire = MargaretYamlStorage.getDaysToExpire();
  private final int proposalTimeout = MargaretYamlStorage.getProposalTimeout();

  private final List<Object> defaultSettingsValues =
      PlayerSettings.getDatabaseSettings()
          .values()
          .stream()
          .map(EnumSettings::getDefaultValue)
          .collect(Collectors.toList());
  private final AsyncMojangResolver resolver;

  public PlayerObjectBuilder(PluginCore core) {
    this.core = core;
    this.dbTasks = core.getDatabaseTask();
    this.resolver = MojangResolver.newAsyncResolver(
        Executors.newSingleThreadExecutor());
  }

  public MargaretPlayer createMargaretPlayer(PlayerData playerData,
                                             MargaretPlayerMeta metaSettings) {
    CacheSet<Proposal> cacheSet = new CacheSetImpl<>(
        proposalTimeout,
        Caffeine.newBuilder()
            .maximumSize(maxProposals)
            .writer(new ProposalWriter()));
    return new MargaretPlayerImpl(playerData, metaSettings, cacheSet);
  }

  public PlayerData initPlayerData(Player player) {
    UUID uuid = player.getUniqueId();
    Map<String, SqlObject> databaseMeta = dbTasks.getPlayerData(uuid);
    PlayerData playerData;
    if (CommonsUtils.isNullOrEmpty(databaseMeta)) {
      playerData = defaultPlayerData(player);
      dbTasks.setupPlayerData(defaultPlayerData(player), daysToExpire);
    } else {
      dbTasks.updatePlayerExpire(uuid, daysToExpire);
      playerData = createPlayerData(player, databaseMeta);
    }
    return playerData;
  }

  public MargaretPlayerMeta initPlayerSettings(UUID uuid) {
    Map<String, SqlObject> playerSettings = dbTasks.getPlayerSettings(uuid);
    if (CommonsUtils.isNullOrEmpty(playerSettings)) {
      dbTasks.setupPlayerSettings(uuid, defaultSettingsValues);
    }
    return refillSettings(uuid, playerSettings);
  }

  private PlayerData createPlayerData(Player player,
                                      Map<String, SqlObject> metadata) {
    if (CommonsUtils.isNullOrEmpty(metadata)) {
      throw new NullPointerException("Unexpected null or empty of PlayerData.");
    }

    SqlObject name = metadata.get("name");
    SqlObject partnerId = metadata.get("partnerid");
    SqlObject gender = metadata.get("gender");
    Statements.checkObjects(
        "Metadata is null.",
        name, partnerId, gender);
    String playerName = MargaretYamlStorage.isPlayerAuthentication()
        ? checkName(player, name.getString())
        : updateNameAndGet(player.getUniqueId(), name.getString());
    Partnership partnership = createPartner(
        player,
        partnerId.getString());
    return new PlayerData(
        player.getUniqueId(),
        playerName,
        partnership,
        gender.getString());
  }

  private String checkName(Player player, String playerNameDB) {
    try {
      if (!playerNameDB.equals(player.getName())) {
        MojangAccount account = resolver.resolveAccount(player.getName());
        if (!account.getName().equals(player.getName()) ||
            !account.getId().equals(player.getUniqueId().toString())) {
          throw new IllegalStateException(
              "Player name not match in Mojang's api " +
                  "(Player's UUID: " + player.getUniqueId() +
                  ", name: " + player.getName() +
                  ", Mojang's Account UUID: " + account.getId() +
                  ", name: " + account.getName());
        }

        MessageUtils.debug("Update name " + playerNameDB +
            " of UUID " + player.getUniqueId() +
            " to " + player.getName());
        return updateNameAndGet(
            player.getUniqueId(),
            account.getName());
      }
    } catch (InterruptedException |
        ExecutionException |
        TimeoutException |
        IllegalStateException e) {
      throw new PlayerVerificationException(e);
    }
    return null;
  }

  private String updateNameAndGet(UUID uuid, String name) {
    dbTasks.updatePlayerData("name",
        name,
        uuid);
    return name;
  }

  public Partnership createPartner(Player player,
                                   String partnerId) {
    Partnership partnership;
    if (!partnerId.equalsIgnoreCase(
        EmptyObjects.getEmptyPartnerId())) {
      Partnerships.loadPartner(partnerId);
      partnership = core.getPartner(partnerId);
      if (!partnership.isEmpty()) {
        Partnerships.reloadPartnerPermission(
            player, partnership, true);
      }
    } else {
      partnership = EmptyObjects.getEmptyPartner();
    }
    return partnership;
  }

  private PlayerData defaultPlayerData(Player player) {
    return new PlayerData(
        player.getUniqueId(),
        player.getName(),
        EmptyObjects.getEmptyPartner(),
        DefaultGender.UNKNOWN.getName());
  }

  private MargaretPlayerMeta refillSettings(UUID uuid, Map<String, SqlObject> resultMap) {
    // Clone map to mutate the information
    Map<String, Boolean> settings = new HashMap<>();
    Map<String, SqlObject> mutable = Maps.newHashMap(resultMap);

    try {
      for (Map.Entry<String, SqlObject> entry : mutable.entrySet()) {
        settings.put(entry.getKey(), entry.getValue().getBoolean());
      }
    } catch (SQLCastException e) {
      MessageUtils.warning("Player's " + uuid
          + " settings table doesn't contain boolean values...?");
      e.printStackTrace();
    }

    for (EnumSettings setting : PlayerSettings.getAllSettings().values()) {
      settings.putIfAbsent(setting.getName(), setting.getDefaultValue());
    }
    return new MargaretPlayerMeta(settings);
  }
}
