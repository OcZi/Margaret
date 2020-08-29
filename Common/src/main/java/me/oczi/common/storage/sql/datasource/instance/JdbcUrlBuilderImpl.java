package me.oczi.common.storage.sql.datasource.instance;

import me.oczi.common.api.configuration.DataSourceCredentials;
import me.oczi.common.storage.sql.datasource.DataSourceType;
import me.oczi.common.storage.sql.datasource.DataSourceTypePackage;

import java.io.File;
import java.io.IOException;

import static me.oczi.common.storage.sql.datasource.DataSourceType.H2;

class JdbcUrlBuilderImpl implements JdbcUrlBuilder {
  private final DataSourceType dataSourceType;
  private final String driverClassName;
  private final String mode;
  private final String jdbcUrl;

  private DataSourceCredentials credentials;
  private File dataFolder;

  private boolean createFiles = true;

  public JdbcUrlBuilderImpl(DataSourceTypePackage dataSourceTypePackage) {
    this.dataSourceType = dataSourceTypePackage.getDataSourceType();
    this.driverClassName = dataSourceTypePackage.getDriverClassName();
    this.mode = dataSourceType.checkMode(
        dataSourceTypePackage.getMode());
    this.jdbcUrl = dataSourceType.getJdbcUrl(mode);
  }

  private String switchJdbcUrl(DataSourceType databaseType) {
    return mode.equalsIgnoreCase("server")
        ? serverJdbcUrl(jdbcUrl)
        : embeddedJdbcUrl(jdbcUrl, databaseType);
  }

  private String serverJdbcUrl(String jdbcUrl) {
    if (credentials == null) {
      throw new NullPointerException("Credentials is null.");
    }

    String database = credentials.getDatabase();
    String hostname = credentials.getHostname();
    String port = credentials.getPort();
    return String.format(jdbcUrl, hostname, port, database);
  }

  private String embeddedJdbcUrl(String jdbcUrl,
                                 DataSourceType databaseType) {
    if (dataFolder == null) {
      throw new NullPointerException("DataFolder for Embedded database is null.");
    }

    String path = getUrlAndGenerateFile(databaseType);
    return String.format(jdbcUrl, path);
  }

  private String getUrlAndGenerateFile(DataSourceType dataSourceType) {
    String format = switchLocalDbFormat(dataSourceType);
    String fileName = "database-" + format;
    String folder = createFileFolder(fileName);
    return dataSourceType == H2
        ? folder.replace(".mv.db", "")
        : folder;
  }

  private String createFileFolder(String fileName) {
    File folder = new File(dataFolder, fileName);
    createNewFile(folder);
    return folder.getAbsolutePath();
  }

  private String switchLocalDbFormat(DataSourceType dataSourceType) {
    return dataSourceType.name().toLowerCase() +
          (dataSourceType == H2 ? ".mv.db" : ".db");
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  private void createNewFile(File file) {
    if (createFiles) {
      try {
        file.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public File getFile() {
    return dataFolder;
  }

  @Override
  public JdbcUrlBuilder createFiles(boolean admitted) {
    this.createFiles = admitted;
    return this;
  }

  @Override
  public JdbcUrlBuilder setServerMode(DataSourceCredentials credentials) {
    this.credentials = credentials;
    return this;
  }

  @Override
  public JdbcUrlBuilder setEmbeddedMode(File dataFolder) {
    this.dataFolder = dataFolder;
    return this;
  }

  public DataSourceType getDataSourceType() {
    return dataSourceType;
  }

  public String getMode() {
    return mode;
  }

  public String getDriverClassName() {
    return driverClassName;
  }

  public String getJdbcUrl() {
    return jdbcUrl;
  }

  public DataSourceCredentials getCredentials() {
    return credentials;
  }

  @Override
  public String build() {
    return switchJdbcUrl(dataSourceType);
  }
}
