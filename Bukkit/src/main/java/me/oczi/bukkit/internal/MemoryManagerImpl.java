package me.oczi.bukkit.internal;

import com.github.benmanes.caffeine.cache.Caffeine;
import me.oczi.bukkit.MargaretMain;
import me.oczi.bukkit.objects.collections.PartnerTop;
import me.oczi.bukkit.objects.collections.PartnerTopImpl;
import me.oczi.common.api.configuration.CacheConfig;

import java.util.concurrent.TimeUnit;

public class MemoryManagerImpl implements MemoryManager {
  private final ObjectCachePlugin persistenceCache;
  private ObjectCachePlugin garbageCache;

  private final PartnerTop top;

  public MemoryManagerImpl(CacheConfig config) {
    this.persistenceCache = new ObjectCachePluginImpl(
        Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.DAYS)
            .build(),
        Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.DAYS)
            .build());
    if (config.isGarbage()) {
      createGarbageCache(config);
    }
    this.top = new PartnerTopImpl(
        config.getPartnerTopLimit(),
        config.getPartnerTimeOut(),
        // Hardcoded DbTasks
        // At this point, database is loaded.
        MargaretMain.getCore().getDatabaseTask());
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

  @Override
  public PartnerTop getPartnerTop() {
    return top;
  }
}
