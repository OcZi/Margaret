package me.oczi.common.storage.sql.datasource.instance;

import me.oczi.common.api.configuration.DataSourceCredentials;
import me.oczi.common.api.state.BuildableState;

import java.io.File;

public interface JdbcUrlBuilder extends BuildableState<String> {

  JdbcUrlBuilder createFiles(boolean admitted);

  JdbcUrlBuilder setServerMode(DataSourceCredentials credentials);

  JdbcUrlBuilder setEmbeddedMode(File dataFolder);
}
