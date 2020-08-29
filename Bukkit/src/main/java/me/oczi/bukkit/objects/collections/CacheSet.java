package me.oczi.bukkit.objects.collections;

import com.github.benmanes.caffeine.cache.Cache;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

public interface CacheSet<K> extends Set<K> {

  Long getDateOf(K key);

  Long getExpireOf(K key);

  boolean containsAsMap(K key);

  void addAll(Map<K, Long> toAdd);

  void removeAll();

  Set<K> values();

  @Deprecated
  Set<K> valuesByDate();

  long mapSize();

  Cache<K, Long> asCache();

  ConcurrentMap<K, Long> asMap();
}