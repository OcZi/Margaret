package me.oczi.bukkit.internal.objectcycle.player;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.collect.Maps;
import me.oczi.bukkit.PluginCore;
import me.oczi.bukkit.internal.database.DbTasks;
import me.oczi.bukkit.objects.Proposal;
import me.oczi.bukkit.objects.collections.CacheSet;
import me.oczi.bukkit.objects.collections.CacheSetImpl;
import me.oczi.bukkit.objects.partner.Partner;
import me.oczi.bukkit.objects.player.MargaretPlayer;
import me.oczi.bukkit.objects.player.MargaretPlayerImpl;
import me.oczi.bukkit.objects.player.MargaretPlayerMeta;
import me.oczi.bukkit.objects.player.PlayerData;
import me.oczi.bukkit.other.ProposalWriter;
import me.oczi.bukkit.storage.yaml.MargaretYamlStorage;
import me.oczi.bukkit.utils.DefaultGender;
import me.oczi.bukkit.utils.EmptyObjects;
import me.oczi.bukkit.utils.MessageUtils;
import me.oczi.bukkit.utils.Partners;
import me.oczi.bukkit.utils.settings.EnumSettings;
import me.oczi.bukkit.utils.settings.PlayerSettings;
import me.oczi.common.exceptions.SQLCastException;
import me.oczi.common.storage.sql.orm.result.ResultMap;
import me.oczi.common.storage.sql.orm.result.SqlObject;
import me.oczi.common.utils.CommonsUtils;
import me.oczi.common.utils.Statements;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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

  public PlayerObjectBuilder(PluginCore core) {
    this.core = core;
    this.dbTasks = core.getDatabaseTask();
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
    ResultMap databaseMeta = dbTasks.getPlayerData(uuid);
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
    ResultMap playerSettings = dbTasks.getPlayerSettings(uuid);
    if (CommonsUtils.isNullOrEmpty(playerSettings)) {
      dbTasks.setupPlayerSettings(uuid, defaultSettingsValues);
    }
    return refillSettings(uuid, playerSettings);
  }

  private PlayerData createPlayerData(Player player,
                                      ResultMap metadata) {
    if (CommonsUtils.isNullOrEmpty(metadata)) {
      throw new NullPointerException("Unexpected null or empty of PlayerData.");
    }

    SqlObject name = metadata.get("name");
    SqlObject partnerId = metadata.get("partnerid");
    SqlObject gender = metadata.get("gender");
    Statements.checkObjects(
        "Metadata is null.",
        name, partnerId, gender);

    Partner partner = createPartner(
        player,
        partnerId.getString());
    return new PlayerData(
        player.getUniqueId(),
        name.getString(),
        partner,
        gender.getString());
  }

  public Partner createPartner(Player player,
                               String partnerId) {
    Partner partner;
    if (!partnerId.equalsIgnoreCase(
        EmptyObjects.getEmptyPartnerId())) {
      Partners.loadPartner(partnerId);
      partner = core.getPartner(partnerId);
      if (!partner.isEmpty()) {
        Partners.reloadPartnerPermission(
            player, partner, true);
      }
    } else {
      partner = EmptyObjects.getEmptyPartner();
    }
    return partner;
  }

  private PlayerData defaultPlayerData(Player player) {
    return new PlayerData(
        player.getUniqueId(),
        player.getName(),
        EmptyObjects.getEmptyPartner(),
        DefaultGender.UNKNOWN.getName());
  }

  private MargaretPlayerMeta refillSettings(UUID uuid, ResultMap resultMap) {
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
