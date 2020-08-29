package me.oczi.common.api.configuration;

public interface DataSourceConfig
    extends DataSourceCredentials{

  String getMode();

  boolean isServerMode();

  boolean isEmbeddedMode();
}
