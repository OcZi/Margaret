package me.oczi.bukkit.internal;

import com.github.benmanes.caffeine.cache.Caffeine;
import me.oczi.common.api.configuration.CacheConfig;

import java.util.concurrent.TimeUnit;

public class MemoryManagerImpl implements MemoryManager {
  private final ObjectCachePlugin persistenceCache;
  private ObjectCachePlugin garbageCache;

  public MemoryManagerImpl(CacheConfig config) {
    persistenceCache = new ObjectCachePluginImpl(
        Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.DAYS)
            .build(),
        Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.DAYS)
            .build());
    if (config.isGarbage()) {
      createGarbageCache(config);
    }
  }

  private void createGarbageCache(CacheConfig config) {
    int playerTimeout = config.getPlayerTimeOut();
    int partnerTimeout = config.getPartnerTimeOut();
    garbageCache = new ObjectCachePluginImpl(
        Caffeine.newBuilder()
            .expireAfterWrite(playerTimeout, TimeUnit.SECONDS)
            .build(),
        Caffeine.newBuilder()
            .expireAfterWrite(partnerTimeout, TimeUnit.SECONDS)
            .build());
  }

  @Override
  public boolean isGarbageCache() {
    return garbageCache != null;
  }

  @Override
  public ObjectCachePlugin getPersistenceCache() {
    return persistenceCache;
  }

  @Override
  public ObjectCachePlugin getGarbageCache() {
    return garbageCache;
  }
}
