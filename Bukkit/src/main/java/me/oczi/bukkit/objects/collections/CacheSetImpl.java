package me.oczi.bukkit.objects.collections;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import me.oczi.common.utils.CommonsUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * A inflexible collection-like to create {@link Cache} as a Set.
 */
public class CacheSetImpl<K> implements CacheSet<K> {
  private final Cache<K, Long> cooldownMap;
  private final long millisecondsToExpire;

  public CacheSetImpl(int secondsToExpire) {
    this.millisecondsToExpire = secondsToExpire * 1000;
    this.cooldownMap = Caffeine.newBuilder()
        .expireAfterWrite(millisecondsToExpire, TimeUnit.MILLISECONDS)
        .build();
  }

  public CacheSetImpl(int secondsToExpire,
                      Caffeine<K, Long> builder) {
    this.millisecondsToExpire = secondsToExpire * 1000;
    this.cooldownMap = builder
        .expireAfterWrite(millisecondsToExpire, TimeUnit.MILLISECONDS)
        .build();
  }

  @Override
  public int size() {
    return asMap().keySet().size();
  }

  @Override
  public boolean isEmpty() {
    return CommonsUtils.isNullOrEmpty(cooldownMap);
  }

  @Override
  public boolean contains(Object o) {
    return asMap().containsKey(o);
  }

  @Override
  public boolean containsAsMap(K key) {
    return asMap().containsKey(key);
  }

  @NotNull
  @Override
  public Iterator<K> iterator() {
    return asMap().keySet().iterator();
  }

  @NotNull
  @Override
  public Object[] toArray() {
    return asMap().keySet().toArray();
  }

  @SuppressWarnings("SuspiciousToArrayCall")
  @NotNull
  @Override
  public <T> T[] toArray(@NotNull T[] ts) {
    return asMap().keySet().toArray(ts);
  }

  @Override
  public boolean add(K key) {
    cooldownMap.put(key, System.currentTimeMillis());
    return true;
  }

  @Override
  public void addAll(Map<K, Long> toAdd) {
    cooldownMap.putAll(toAdd);
  }

  @Override
  public boolean remove(Object key) {
    cooldownMap.invalidate(key);
    return true;
  }

  @Override
  public boolean containsAll(@NotNull Collection<?> collection) {
    return asMap().keySet().containsAll(collection);
  }

  @Override
  public boolean addAll(@NotNull Collection<? extends K> collection) {
    for (K k : collection) { add(k); }
    return true;
  }

  @Override
  public boolean retainAll(@NotNull Collection<?> collection) {
    return asMap().keySet().retainAll(collection);
  }

  @Override
  public boolean removeAll(@NotNull Collection<?> collection) {
    cooldownMap.invalidateAll(collection);
    return true;
  }

  @Override
  public void clear() {
    cooldownMap.cleanUp();
  }

  @Override
  public void removeAll() {
    cooldownMap.invalidateAll();
  }

  @Override
  public Long getExpireOf(K key) {
    clear();
    Long expire = cooldownMap.getIfPresent(key);
    return expire != null
        ? expire + millisecondsToExpire
        : 0;
  }

  @Override
  public Long getDateOf(K key) {
    return cooldownMap.getIfPresent(key);
  }

  @Override
  public Set<K> values() {
    return cooldownMap.asMap().keySet();
  }

  @Override
  public Set<K> valuesByDate() {
    return cooldownMap.asMap().entrySet().stream()
        .sorted(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey)
        .collect(Collectors.toCollection(LinkedHashSet::new));
  }

  @Override
  public long mapSize() {
    return cooldownMap.estimatedSize();
  }

  @Override
  public Cache<K, Long> asCache() {
    return cooldownMap;
  }

  @Override
  public ConcurrentMap<K, Long> asMap() {
    return cooldownMap.asMap();
  }
}
