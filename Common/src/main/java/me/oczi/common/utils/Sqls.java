package me.oczi.common.utils;

import me.oczi.common.exceptions.SQLNotSafeException;
import me.oczi.common.exceptions.SQLRuntimeException;
import me.oczi.common.storage.sql.orm.result.QueryMap;
import me.oczi.common.storage.sql.orm.result.ResultMap;
import me.oczi.common.storage.sql.orm.result.SqlObject;
import me.oczi.common.storage.sql.orm.result.SqlObjectImpl;

import java.sql.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Sqls {

  /**
   * Converts all the content of ResultSet to Map.
   * @param rs - ResultSet to Map
   * @param <T> - Type of Map Value
   * @return ResultSet to Map.
   */
  static <T> Map<String, T> mapResultSetWithType(ResultSet rs, Class<T> type) {
    Map<String, T> map = null;
    try {
      int columns = rs.getMetaData().getColumnCount();
      map = new HashMap<>(1 + (int) (columns / 0.75));
      int count = 0;
      while (rs.next()) {
        count++;
        for (int i = 1; i <= columns; i++) {
          // Lowercase the columns to avoid problems
          // with H2 uppercase names.
          String column = rs.getMetaData().getColumnName(i).toLowerCase();
          T object = rs.getObject(column, type);
          if (map.containsKey(column)) {
            column += "-" + i;
          }
          map.put(column, object);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return map == null
        ? Collections.emptyMap()
        : map;
  }

  /**
   * Converts all the content of ResultSet to Map.
   * @param rs - ResultSet to Map
   * @param <T> - Type of Map Value
   * @return ResultSet to Map.
   */
  @SuppressWarnings("unchecked")
  static <T> Map<String, T> mapResultSetWithCast(ResultSet rs) {
    Map<String, T> map = null;
    try {
      int columns = rs.getMetaData().getColumnCount();
      map = new HashMap<>(1 + (int) (columns / 0.75));
      int count = 0;
      while (rs.next()) {
        count++;
        for (int i = 1; i <= columns; i++) {
          // Lowercase the columns to avoid problems
          // with H2 uppercase names.
          String column = rs.getMetaData().getColumnName(i).toLowerCase();
          T object = (T) rs.getString(column);
          if (map.containsKey(column)) {
            column += "-" + i;
          }
          map.put(column, object);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return map == null
        ? Collections.emptyMap()
        : map;
  }

  /**
   * Converts all the content of ResultSet to Map.
   * @param rs - ResultSet to Map
   * @return ResultSet to Unmodifiable Map.
   */
  static ResultMap newResultMap(ResultSet rs) {
    Map<String, SqlObject> map;
    int rows = 0;
    int columns;
    try {
      ResultSetMetaData metaData = rs.getMetaData();
      columns = metaData.getColumnCount();
      map = new HashMap<>();
      while (rs.next()) {
        rows++;
        for (int i = 1; i <= columns; i++) {
          // Lowercase the columns to avoid problems
          // with H2 uppercase names.
          String column = metaData
              .getColumnName(i).toLowerCase();
          SqlObject sqlObject = new SqlObjectImpl(i, rs);
          if (map.containsKey(column)) {
            column += "-" + rows;
          }
          map.put(column, sqlObject);
        }
      }
    } catch (SQLException e) {
      throw new SQLRuntimeException("Error while create a new ResultMap", e);
    }

    return new QueryMap(map, columns, rows);
  }

  static void setObjectsStatement(PreparedStatement statement,
                                  List<Object> params) {
    setObjectsStatement(statement, params, false);
  }

  static void setObjectsStatement(PreparedStatement statement,
                                  List<Object> params,
                                  boolean nullSafe) {
    if (!CommonsUtils.isNullOrEmpty(params)) {
      for (int i = 0; i < params.size(); i++) {
        setObjectStatement(statement, i + 1, params.get(i), nullSafe);
      }
    }
  }

  static void setObjectStatement(PreparedStatement statement,
                                 int i,
                                 Object param) {
    setObjectStatement(statement, i, param, false);
  }

  static void setObjectStatement(PreparedStatement statement,
                                 int i,
                                 Object param,
                                 boolean nullSafe) {
    try {
      if (param != null) {
        statement.setObject(i, param);
      } else {
        if (nullSafe) {
          statement.setNull(i, Types.NULL);
          return;
        }

        throw new SQLNotSafeException(
            "Connection not accept null types. Index: " + i);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
