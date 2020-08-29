package me.oczi.bukkit.internal.objectcycle;

import me.oczi.bukkit.MargaretMain;
import me.oczi.bukkit.PluginCore;
import me.oczi.bukkit.internal.MemoryManager;
import me.oczi.bukkit.internal.ObjectCachePlugin;

public abstract class AbstractObjectLoader<K> implements ObjectLoader<K> {
  protected final PluginCore core;
  protected final MemoryManager cache;

  protected final ObjectCachePlugin persistenceCache;
  protected final ObjectCachePlugin garbageCache;

  protected AbstractObjectLoader(MemoryManager cache) {
    // Hardcoded core
    this.core = MargaretMain.getCore();
    this.cache = cache;
    this.persistenceCache = cache.getPersistenceCache();
    this.garbageCache = cache.getGarbageCache();
  }
}