package me.oczi.common.api.sql;

public interface SqlTable {

  String getName();

  String[] getColumns();

  // Bad method.
  default String getConstraint() {
    return "";
  }
}
