package me.oczi.bukkit.storage.yaml.impl;

import me.oczi.common.api.configuration.CacheConfig;
import org.bukkit.configuration.file.FileConfiguration;

import java.time.Duration;

public class CacheConfigImpl implements CacheConfig {
  private final boolean garbage;
  private final int playerTimeout;
  private final int partnerTimeout;

  private final long partnerTopRefresh;
  private final int partnerTopMaxEntries;
  private final int partnerTopEntriesPerPage;

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
    this.partnerTopEntriesPerPage = config.getInt(
        "cache-entries-per-page", 10);
    this.partnerTopMaxEntries = config.getInt(
        "cache-max-entries", 20);
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
  public int getPartnerTopMaxEntries() {
    return partnerTopMaxEntries;
  }

  @Override
  public int getPartnerTopEntriesPerPage() {
    return partnerTopEntriesPerPage;
  }
}
