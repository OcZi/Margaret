package me.oczi.bukkit.objects.collections;

import java.util.Map;

/**
 * A map that always the value will be boolean.
 * @param <K> Key type.
 */
public interface MetaMap<K> {

  void toggleMeta(K key);

  boolean isMeta(K key);

  void updateMeta(K key, boolean value);

  boolean containsMeta(K key);

  Map<K, Boolean> asMap();
}
