package me.oczi.common.storage.sql.dsl.result;

public class SqlObjectMetaDataImpl implements SqlObjectMetaData {
  private final String columnName;
  private final String typeName;
  private final String columnClassName;
  private final String tableName;
  private final String catalogName;

  public SqlObjectMetaDataImpl(String columnName,
                               String typeName,
                               String columnClassName,
                               String tableName,
                               String catalogName) {
    this.columnName = columnName;
    this.typeName = typeName;
    this.columnClassName = columnClassName;
    this.tableName = tableName;
    this.catalogName = catalogName;
  }

  @Override
  public String getColumnName() {
    return columnName;
  }

  @Override
  public String getColumnClassName() {
    return columnClassName;
  }

  @Override
  public String getCatalogName() {
    return catalogName;
  }

  @Override
  public String getTypeName() {
    return typeName;
  }

  @Override
  public String getTableName() {
    return tableName;
  }

  @Override
  public String toString() {
    return "SqlObjectMetaDataImpl{" +
        "columnName='" + columnName + '\'' +
        ", typeName='" + typeName + '\'' +
        ", columnClassName='" + columnClassName + '\'' +
        ", tableName='" + tableName + '\'' +
        ", catalogName='" + catalogName + '\'' +
        '}';
  }
}
