package me.oczi.common.storage.sql.datasource.instance;

import me.oczi.common.api.configuration.DataSourceCredentials;
import me.oczi.common.api.configuration.HkDataSourceConfig;
import me.oczi.common.api.configuration.DataSourceProperties;
import me.oczi.common.api.state.BuildableState;
import me.oczi.common.storage.sql.datasource.DataSource;

import java.util.Properties;

public interface DataSourceCPBuilder extends BuildableState<DataSource> {

  DataSourceCPBuilder setConfig(HkDataSourceConfig dbConfig);

  DataSourceCPBuilder setCredentials(DataSourceCredentials credentials);

  DataSourceCPBuilder setProperties(DataSourceProperties properties);

  DataSourceCPBuilder setProperties(Properties properties);
}
