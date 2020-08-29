package me.oczi.common.storage.sql.function;

import me.oczi.common.storage.sql.datasource.DataSourceType;

/**
 * Early enum of Sql Functions.
 */
public enum SqlFunctionPattern {
  CURRENT_DATE("CURDATE()",
      "CURRENT_DATE()");

  private final String mysql;
  private final String postgresql;

  SqlFunctionPattern(String mysql, String postgresql) {
    this.mysql = mysql;
    this.postgresql = postgresql;
  }

  public String getMySQL() {
    return mysql;
  }

  public String getPostgresql() {
    return postgresql;
  }

  public String getByDatabase(DataSourceType type) {
    switch (type) {
      case POSTGRESQL:
        return postgresql;
      case SQLITE:
      case HSQLDB:
      case MYSQL:
      case H2:
      default:
        return getMySQL();
    }
  }
}
