package me.oczi.bukkit.storage.yaml.impl;

import me.oczi.common.api.configuration.CacheConfig;
import org.bukkit.configuration.file.FileConfiguration;

public class CacheConfigImpl implements CacheConfig {
  private final boolean garbage;
  private final int playerTimeout;
  private final int partnerTimeout;

  public CacheConfigImpl(FileConfiguration config) {
    garbage = config.getBoolean(
        "cache.create-garbage-cache", false);
    playerTimeout = config.getInt(
        "cache.cache-player-timeout", 120);
    partnerTimeout = config.getInt(
        "cache.cache-partner-timeout", 120);
  }

  @Override
  public boolean isGarbage() {
    return garbage;
  }

  @Override
  public int getPlayerTimeOut() {
    return playerTimeout;
  }

  @Override
  public int getPartnerTimeOut() {
    return partnerTimeout;
  }
}
