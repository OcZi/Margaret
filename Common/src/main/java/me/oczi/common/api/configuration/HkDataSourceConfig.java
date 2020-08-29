package me.oczi.common.api.configuration;

public interface HkDataSourceConfig {

  String getConnectionTestQuery();

  long getConnectionTimeout();

  long getIdleTimeout();

  long getMaxLifetime();

  int getMinimumIdle();

  int getMaximumPoolSize();
}
