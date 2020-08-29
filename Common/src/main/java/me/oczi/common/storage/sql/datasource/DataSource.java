package me.oczi.common.storage.sql.datasource;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.SQLException;

public interface DataSource extends Closeable {

  Connection getConnection() throws SQLException;

  void close();
}
