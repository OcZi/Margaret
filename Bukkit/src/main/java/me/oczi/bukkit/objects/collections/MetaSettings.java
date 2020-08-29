package me.oczi.bukkit.objects.collections;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of {@link MetaMap}.
 * @param <K> Key type.
 */
public class MetaSettings<K> implements MetaMap<K>{
  protected Map<K, Boolean> metaMap;

  public MetaSettings() {
    this.metaMap = new HashMap<>();
  }

  public MetaSettings(Map<K, Boolean> metaMap) {
    this.metaMap = metaMap;
  }

  @Override
  public void toggleMeta(K key) {
    boolean value = metaMap.get(key);
    updateMeta(key, !value);
  }

  @Override
  public boolean isMeta(K key) {
    return metaMap.get(key);
  }

  @Override
  public void updateMeta(K key, boolean value) {
    metaMap.replace(key, value);
  }

  @Override
  public boolean containsMeta(K key) {
    return metaMap.containsKey(key);
  }

  @Override
  public Map<K, Boolean> asMap() {
    return metaMap;
  }
}
