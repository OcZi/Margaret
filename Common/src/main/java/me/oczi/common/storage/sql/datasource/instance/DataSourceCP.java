package me.oczi.common.storage.sql.datasource.instance;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.oczi.common.storage.sql.datasource.DataSource;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.SQLException;

public final class DataSourceCP implements DataSource {
  private final HikariDataSource hkDataSource;

  public DataSourceCP() { this(null); }

  public DataSourceCP(@Nullable HikariConfig hkConfig) {
    this.hkDataSource = hkConfig != null
        ? new HikariDataSource(hkConfig)
        : new HikariDataSource();
  }

  public HikariDataSource getDataSource() {
    return hkDataSource;
  }

  @Override
  public void close() {
    if (!hkDataSource.isClosed()) hkDataSource.close();
  }

  @Override
  public Connection getConnection() throws SQLException {
    return hkDataSource.getConnection();
  }
}
