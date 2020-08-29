package me.oczi.bukkit.storage.yaml.impl;

import me.oczi.bukkit.storage.yaml.MargaretYamlStorage;
import me.oczi.common.api.configuration.HkDataSourceConfig;
import org.bukkit.configuration.file.FileConfiguration;

public class HkDataSourceConfigImpl implements HkDataSourceConfig {

  //Hikari Configuration
  private final String connectionTestQuery;

  private final long connectionTimeout, idleTimeout, maxLifetime;
  private final int minimumIdle, maximumPoolSize;

  public HkDataSourceConfigImpl(FileConfiguration config) {

    this.connectionTestQuery = config
        .getString("hikariconfig.connection-test-query",
                    "SELECT 1");

    this.connectionTimeout = config
        .getLong("hikariconfig.connection-timeout",
                  30000);
    this.idleTimeout = config
        .getLong("hikariconfig.idle-timeout",
                  600000);
    this.maxLifetime = config
        .getLong("hikariconfig.max-lifetime",
                  1800000);

    this.minimumIdle = config
        .getInt("hikariconfig.minimum-idle",
                  10);
    this.maximumPoolSize = config
        .getInt("hikariconfig.maximum.pool-size",
                  10);
  }

  public String getConnectionTestQuery() {
    return connectionTestQuery;
  }

  public long getConnectionTimeout() {
    return connectionTimeout;
  }

  public long getIdleTimeout() {
    return idleTimeout;
  }

  public long getMaxLifetime() {
    return maxLifetime;
  }

  public int getMinimumIdle() {
    return minimumIdle;
  }

  public int getMaximumPoolSize() {
    return maximumPoolSize;
  }
}
