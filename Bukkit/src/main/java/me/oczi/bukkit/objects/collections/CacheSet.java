package me.oczi.bukkit.objects.collections;

import com.github.benmanes.caffeine.cache.Cache;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * A inflexible collection-like to create {@link Cache} as a Set.
 */
public interface CacheSet<K> extends Set<K> {

  Long getDateOf(K key);

  Long getExpireOf(K key);

  boolean containsAsMap(K key);

  void addAll(Map<K, Long> toAdd);

  void removeAll();

  Set<K> values();

  @Deprecated
  Set<K> valuesByDate();

  /**
   * Get map size.
   * @return Size of map.
   */
  long mapSize();

  /**
   * Get CatheSet as {@link Cache}.
   * @return A caffeine cache.
   */
  Cache<K, Long> asCache();

  /**
   * Get CacheSet as {@link ConcurrentMap} of {@link Cache}.
   * @return ConcurrentMap of cache.
   */
  ConcurrentMap<K, Long> asMap();
}