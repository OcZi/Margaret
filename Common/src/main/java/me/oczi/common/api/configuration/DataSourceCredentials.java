package me.oczi.common.api.configuration;

public interface DataSourceCredentials {

  String getUsername();

  String getPassword();

  String getDatabase();

  String getHostname();

  String getPort();
}
