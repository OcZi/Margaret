package me.oczi.common.storage.sql.datasource;

public enum JdbcUrlType {
  EMBEDDED("jdbc:{database-url}:%s"),
  SERVER("jdbc:{database-url}://%s:%s/%s");

  private final String url;

  JdbcUrlType(String url) {
    this.url = url;
  }

  public static JdbcUrlType getJdbcUrlByName(String urlName) {
    return urlName.equalsIgnoreCase("embedded")
        ? EMBEDDED
        : SERVER;
  }

  public String getUrl() {
    return url;
  }
}
