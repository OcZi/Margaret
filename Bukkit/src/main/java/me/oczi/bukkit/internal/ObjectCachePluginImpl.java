package me.oczi.bukkit.internal;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.LoadingCache;
import me.oczi.bukkit.objects.partnership.Partnership;
import me.oczi.bukkit.objects.player.MargaretPlayer;
import me.oczi.bukkit.utils.EmptyObjects;

import java.util.Map;
import java.util.UUID;

/**
 * Two {@link LoadingCache} to collect all
 * MargaretPlayer and Partner.
 */
public class ObjectCachePluginImpl implements ObjectCachePlugin {
  private final Cache<UUID, MargaretPlayer> playerCache;
  private final Cache<String, Partnership> partnerCache;

  public ObjectCachePluginImpl(
               Cache<UUID, MargaretPlayer> playerCache,
               Cache<String, Partnership> partnerCache) {
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
  public Partnership getPartner(String id) {
    return partnerCacheAsMap()
        .getOrDefault(id,
            EmptyObjects.getEmptyPartner());
  }

  @Override
  public void putMargaretPlayer(UUID uuid, MargaretPlayer mgPlayer) {
    playerCacheAsMap().put(uuid, mgPlayer);
  }

  @Override
  public void putPartner(String id, Partnership partnership) {
    partnerCacheAsMap().put(id, partnership);
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
  public Map<String, Partnership> partnerCacheAsMap() {
    return partnerCache.asMap();
  }
}
