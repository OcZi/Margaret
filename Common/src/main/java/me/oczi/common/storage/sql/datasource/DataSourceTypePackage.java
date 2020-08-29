package me.oczi.common.storage.sql.datasource;

import me.oczi.common.api.configuration.DataSourceConfig;

public class DataSourceTypePackage {

  private final DataSourceConfig config;
  private final DataSourceType dataSourceType;
  private final String driverClassName, mode;

  public DataSourceTypePackage(DataSourceConfig config,
                               DataSourceType dataSourceType) {
    this.config = config;
    this.dataSourceType = dataSourceType;
    this.driverClassName = dataSourceType.getDriverClassName();
    this.mode = config.getMode();
  }

  public DataSourceType getDataSourceType() {
    return dataSourceType;
  }

  public DataSourceConfig getConfig() {
    return config;
  }

  public String getDriverClassName() {
    return driverClassName;
  }

  public String getMode() {
    return mode;
  }

  public boolean isServerMode() {
    return mode.equalsIgnoreCase("server");
  }

  public boolean isEmbeddedMode() {
    return mode.equalsIgnoreCase("embedded");
  }
}
