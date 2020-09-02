package me.oczi.bukkit.storage.yaml.impl;

import me.oczi.common.api.configuration.CacheConfig;
import org.bukkit.configuration.file.FileConfiguration;

import java.time.Duration;

public class CacheConfigImpl implements CacheConfig {
  private final boolean garbage;
  private final int playerTimeout;
  private final int partnerTimeout;

  private final long partnerTopRefresh;
  private final int partnerTopLimit;

  public CacheConfigImpl(FileConfiguration config) {
    this.garbage = config.getBoolean(
        "cache.create-garbage-cache", false);
    this.playerTimeout = config.getInt(
        "cache.cache-player-timeout", 120);
    this.partnerTimeout = config.getInt(
        "cache.cache-partner-timeout", 120);
    this.partnerTopRefresh = config.getLong(
        "cache.cache-partner-top-refresh",
        Duration.ofMinutes(5).getSeconds());

    this.partnerTopLimit = config.getInt(
        "cache.cache-partner-top-limit", 10);
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

  @Override
  public long getPartnerTopRefresh() {
    return partnerTopRefresh;
  }

  @Override
  public int getPartnerTopLimit() {
    return partnerTopLimit;
  }
}
