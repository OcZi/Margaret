package me.oczi.common.storage.sql.datasource.instance;

import me.oczi.common.storage.sql.datasource.DataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Generic DataSource that establish connections with JDBC
 * without Connection pool.
 */
public class GenericDataSource implements DataSource {
  private boolean shutdown = false;

  private Connection connection;
  private final String path;
  private final String driverClass;
  private final String jdbcPrefix;

  public GenericDataSource(String path,
                           String driverClass,
                           String jdbcPrefix) {
    this.path = path;
    this.driverClass = driverClass;
    this.jdbcPrefix = jdbcPrefix;
  }

  public Connection startConnection() {
    return shutdown ? null
        : getDriverWithConnection();
  }

  @Override
  public Connection getConnection() {
    return connectionExist() ? connection : startConnection();
  }

  @Override
  public void close() {
    try {
      connection.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public boolean connectionExist() {
    try {
      return connection != null &&
            !connection.isClosed();
    }catch (SQLException e) {
      e.printStackTrace();
    }

    return false;
  }

  public void shutdown() {
    this.shutdown = true;
    this.connection = null;
  }

  private Connection getDriverWithConnection() {
    try {
      Class.forName(driverClass);
      connection = DriverManager
          .getConnection(jdbcPrefix + path);
    }catch (SQLException | ClassNotFoundException e) {
      e.printStackTrace();
    }

    return connection;
  }
}
