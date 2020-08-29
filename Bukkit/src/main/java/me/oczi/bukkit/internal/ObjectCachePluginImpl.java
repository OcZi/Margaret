package me.oczi.bukkit.internal;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import me.oczi.bukkit.objects.partner.Partner;
import me.oczi.bukkit.objects.player.MargaretPlayer;
import me.oczi.bukkit.utils.EmptyObjects;
import me.oczi.bukkit.utils.MessageUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.logging.Logger;

/**
 * Two {@link LoadingCache} to collect all
 * MargaretPlayer and Partner.
 */
public class ObjectCachePluginImpl implements ObjectCachePlugin {
  private final Cache<UUID, MargaretPlayer> playerCache;
  private final Cache<String, Partner> partnerCache;

  public ObjectCachePluginImpl(
               Cache<UUID, MargaretPlayer> playerCache,
               Cache<String, Partner> partnerCache) {
    this.playerCache = playerCache;
    this.partnerCache = partnerCache;
  }

  @Override
  public MargaretPlayer getMargaretPlayer(UUID uuid) {
    return playerCacheAsMap()
        .getOrDefault(uuid,
            EmptyObjects.getEmptyMargaretPlayer());
  }

  @Override
  public Partner getPartner(String id) {
    return partnerCacheAsMap()
        .getOrDefault(id,
            EmptyObjects.getEmptyPartner());
  }

  @Override
  public void putMargaretPlayer(UUID uuid, MargaretPlayer mgPlayer) {
    playerCacheAsMap().put(uuid, mgPlayer);
  }

  @Override
  public void putPartner(String id, Partner partner) {
    partnerCacheAsMap().put(id, partner);
  }

  @Override
  public void removeMargaretPlayer(UUID uuid) {
    playerCache.invalidate(uuid);
  }

  @Override
  public void removePartner(String id) {
    partnerCache.invalidate(id);
  }

  @Override
  public boolean containsMargaretPlayer(UUID uuid) {
    return playerCacheAsMap().containsKey(uuid);
  }

  @Override
  public boolean containsPartner(String id) {
    return partnerCacheAsMap().containsKey(id);
  }

  @Override
  public Map<UUID, MargaretPlayer> playerCacheAsMap() {
    return playerCache.asMap();
  }

  @Override
  public Map<String, Partner> partnerCacheAsMap() {
    return partnerCache.asMap();
  }
}
