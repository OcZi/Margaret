package me.oczi.common.storage.sql.dsl.result;

public interface SqlObjectMetaData {

  String getColumnName();

  String getColumnClassName();

  String getCatalogName();

  String getTypeName();

  String getTableName();
}
