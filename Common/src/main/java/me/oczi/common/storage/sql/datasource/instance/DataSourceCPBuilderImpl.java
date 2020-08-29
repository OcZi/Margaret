package me.oczi.common.storage.sql.datasource.instance;

import com.zaxxer.hikari.HikariConfig;
import me.oczi.common.api.configuration.DataSourceCredentials;
import me.oczi.common.api.configuration.HkDataSourceConfig;
import me.oczi.common.api.configuration.DataSourceProperties;
import me.oczi.common.utils.CommonsUtils;
import me.oczi.common.storage.sql.datasource.DataSourceTypePackage;
import me.oczi.common.storage.sql.datasource.DataSource;

import java.util.Properties;

class DataSourceCPBuilderImpl implements DataSourceCPBuilder{
  private HikariConfig hkConfig;

  private final String driverClassName;
  private final String jdbcUrl;

  public DataSourceCPBuilderImpl(DataSourceTypePackage dataSourceTypePackage,
                                 String jdbcUrl) {
    this.jdbcUrl = jdbcUrl;
    this.driverClassName = dataSourceTypePackage.getDriverClassName();
    initHikariConfig();
  }

  private void initHikariConfig() {
    this.hkConfig = new HikariConfig();
    CommonsUtils.checkStrings(
        "DriverClassName or JdbcUrl is null or empty.",
        driverClassName, jdbcUrl);

    loadDriverClass(driverClassName);
    hkConfig.setDriverClassName(driverClassName);
    hkConfig.setJdbcUrl(jdbcUrl);
  }

  @Override
  public DataSourceCPBuilder setConfig(HkDataSourceConfig dbConfig) {
    hkConfig.setConnectionTimeout(dbConfig.getConnectionTimeout());
    hkConfig.setIdleTimeout(dbConfig.getIdleTimeout());
    hkConfig.setMaxLifetime(dbConfig.getMaxLifetime());
    hkConfig.setMinimumIdle(dbConfig.getMinimumIdle());
    hkConfig.setMaximumPoolSize(dbConfig.getMaximumPoolSize());
    hkConfig.setConnectionTestQuery(dbConfig.getConnectionTestQuery());
    hkConfig.setPoolName("Margaret-HikariCP");
    return this;
  }

  @Override
  public DataSourceCPBuilder setCredentials(DataSourceCredentials credentials) {
    String username = credentials.getUsername();
    String password = credentials.getPassword();
    CommonsUtils.checkStrings(
        "Name is null or empty.",
        username);

    hkConfig.setUsername(username);
    if (!CommonsUtils.isNullOrEmpty(password)) {
      hkConfig.setPassword(password);
    }
    return this;
  }

  @Override
  public DataSourceCPBuilder setProperties(DataSourceProperties properties) {
    return setProperties(properties.getProperties());
  }

  @Override
  public DataSourceCPBuilder setProperties(Properties properties) {
    hkConfig.setDataSourceProperties(properties);
    return this;
  }

  private void loadDriverClass(String driverClassName) {
    try {
      Class.forName(driverClassName);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  public DataSource build() {
    return new DataSourceCP(hkConfig);
  }

  @Override
  public String toString() {
    return "DataSourceCPBuilderImpl{" +
        "driverClassName='" + driverClassName +
        ", jdbcUrl='" + jdbcUrl + '}';
  }
}
