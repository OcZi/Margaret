package me.oczi.bukkit.internal;

import me.oczi.bukkit.objects.collections.PartnerTop;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link ObjectCachePlugin} manager.
 */
public interface MemoryManager {

  /**
   * Is garbage cache null or not.
   * @return Is garbage exist.
   */
  boolean isGarbageCache();

  /**
   * Get the persistence cache of objects.
   * @return Persistence cache.
   */
  ObjectCachePlugin getPersistenceCache();

  /**
   * Get the garbage cache of objects
   * @return Garbage cache.
   */
  @Nullable ObjectCachePlugin getGarbageCache();

  PartnerTop getPartnerTop();
}
