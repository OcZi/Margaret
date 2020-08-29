package me.oczi.bukkit.storage.yaml.impl;

import me.oczi.common.api.configuration.DataSourceConfig;
import org.bukkit.configuration.file.FileConfiguration;

public class DataSourceConfigImpl implements DataSourceConfig {
  private final String username;
  private final String password;
  private final String database;
  private final String hostname;
  private final String port;
  private final String mode;

  public DataSourceConfigImpl(FileConfiguration config) {
    this.username = config
        .getString("credentials.username",
            "");
    this.password = config
        .getString("credentials.password",
            "");
    this.database = config
        .getString("credentials.database",
            "");
    this.hostname = config
        .getString("credentials.hostname",
            "");
    this.port = config
        .getString("credentials.port",
            "");
    this.mode = config
        .getString("general.database-mode",
            "embedded");
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getDatabase() {
    return database;
  }

  @Override
  public String getHostname() {
    return hostname;
  }

  @Override
  public String getPort() {
    return port;
  }

  @Override
  public String getMode() {
    return mode;
  }

  @Override
  public boolean isServerMode() {
    return mode.equalsIgnoreCase("embedded");
  }

  @Override
  public boolean isEmbeddedMode() {
    return mode.equalsIgnoreCase("server");
  }
}
