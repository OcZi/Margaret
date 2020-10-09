package me.oczi.common.storage.sql.datasource;

import me.oczi.common.utils.CommonsUtils;

import static me.oczi.common.storage.sql.datasource.JdbcUrlType.getJdbcUrlByName;

/**
 * DataSource's Properties.
 */
public enum DataSourceType {
  // Embedded-Server Databases
  HSQLDB("embedded",
      "org.hsqldb.jdbc.JDBCDataSource",
      "org.hsqldb.jdbc.JDBCDriver",
      "hsql",
      "file",
      true),
  H2("embedded",
      "org.h2.jdbcx.JdbcDataSource",
      "org.h2.Driver",
      "tcp",
      "file",
      true),

  // Embedded Databases
  SQLITE("embedded",
      "org.sqlite.SQLiteDataSource",
      "org.sqlite.JDBC"),

  // Server Databases
  POSTGRESQL("server",
      "org.postgresql.ds.PGSimpleDataSource",
      "org.postgresql.Driver"),
  MYSQL("server",
      "com.mysql.jdbc.jdbc2.optional.MysqlDataSource",
      "com.mysql.jdbc.Driver");

  private final String defaultMode;
  private final String dataSourceClass; //Never used. But it is good to have it.
  private final String driverClassName;
  private final String serverPrefix;
  private final String embeddedPrefix;
  private final boolean multiMode;

  DataSourceType(String defaultMode,
                 String dataSourceClass,
                 String driverClass) {
    this(defaultMode,
        dataSourceClass,
        driverClass,
        "",
        "",
        false);
  }

  DataSourceType(String defaultMode,
                 String dataSourceClass,
                 String driverClass,
                 String serverPrefix,
                 String embeddedPrefix,
                 boolean multiMode) {
    this.defaultMode = defaultMode;
    this.dataSourceClass = dataSourceClass;
    this.driverClassName = driverClass;
    this.serverPrefix = serverPrefix;
    this.embeddedPrefix = embeddedPrefix;
    this.multiMode = multiMode;
  }

  public String getServerPrefix() {
    return serverPrefix;
  }

  public String getEmbeddedPrefix() {
    return embeddedPrefix;
  }

  public String getDefaultMode() {
    return defaultMode;
  }

  public static String checkMode(String mode,
                                 DataSourceType dataSourceTypeDefault) {
    String defaultMode =
        dataSourceTypeDefault.getDefaultMode();
    boolean hasMultipleModes =
        dataSourceTypeDefault.hasMultipleModes();
    if (!mode.equalsIgnoreCase(defaultMode) &&
        !hasMultipleModes) {
      return defaultMode;
    }
    return isValidMode(mode) ? mode : defaultMode;
  }

  public String checkMode(String mode) {
    return checkMode(mode, this);
  }

  public String getJdbcUrl(String mode) {
    return formatJdbcUrl(checkMode(mode));
  }

  private String formatJdbcUrl(String mode) {
    String prefix = mode.equalsIgnoreCase("server")
        ? serverPrefix
        : embeddedPrefix;
    String jdbcUrl = getJdbcUrlByName(mode).getUrl();
    String dataSourceName = toString().toLowerCase();
    if (!prefix.isEmpty()) {
      dataSourceName += ":" + prefix;
    }
    return jdbcUrl.replace(
        "{database-url}", dataSourceName);
  }

  public String getDataSourceClass() {
    return dataSourceClass;
  }

  public String getDriverClassName() {
    return driverClassName;
  }

  public static boolean isValidMode(String mode) {
    return !CommonsUtils.isNullOrEmpty(mode) &&
        CommonsUtils.stringEqualsTo(mode,
            "embedded", "server");
  }

  public boolean hasMultipleModes() {
    return multiMode;
  }
}
