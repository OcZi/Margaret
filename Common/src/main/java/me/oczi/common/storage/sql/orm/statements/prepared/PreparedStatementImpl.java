package me.oczi.common.storage.sql.orm.statements.prepared;

import me.oczi.common.api.sql.SqlTable;
import me.oczi.common.exceptions.RequiredMetadataException;
import me.oczi.common.storage.sql.orm.other.DataType;
import me.oczi.common.storage.sql.orm.statements.data.SqlStatementBody;
import me.oczi.common.storage.sql.orm.statements.SqlStatementBodyPackage;
import me.oczi.common.storage.sql.orm.statements.data.StatementBasicData;
import me.oczi.common.storage.sql.orm.statements.data.StatementBasicDataImpl;
import me.oczi.common.storage.sql.orm.statements.data.StatementMetadata;
import me.oczi.common.utils.CommonsUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public final class PreparedStatementImpl
    implements PreparedStatement {
  private String statement;

  private final int requiredColumns;
  private final int requiredParams;

  private StatementBasicData metaData;

  public PreparedStatementImpl(String statement,
                               int requiredColumns,
                               int requiredParams,
                               StatementMetadata metaData) {
    this.statement = statement;
    this.requiredColumns = requiredColumns;
    this.requiredParams = requiredParams;
    this.metaData = new StatementBasicDataImpl(metaData);
  }

  public PreparedStatementImpl(String statement,
                               int requiredColumns,
                               int requiredParams,
                               @Nullable SqlTable table,
                               List<String> cols,
                               List<Object> params) {
    this.statement = statement;
    this.requiredColumns = requiredColumns;
    this.requiredParams = requiredParams;
    this.metaData = new StatementBasicDataImpl(table, cols, params);
  }

  private <E> void checkNumberReached(String str,
                                      int i1,
                                      List<E> list) {
    if (list == null) { return; }

    int i2 = list.size();
    if (i1 > i2) {
      System.out.println(statement);
      throw new RequiredMetadataException(str
          + " not reached (Input: " + i2
          + ", Required: " + i1 + ")");
    }
    // Deleting List overflow.
    if (i1 < i2) {
      int difference = i2 - i1;
      list.removeAll(
          list.subList(i2 - difference, i2));
    }
  }

  @Override
  public SqlPreparedStatementCompiled compile() {
    checkNumberReached("columns",  requiredColumns, getColumns());
    checkNumberReached("parameters",  requiredParams, getParams());
    formatStatement();
    return new SqlPreparedStatementCompiledImpl(this);
  }

  /**
   * Format statement's placeholders with metadata.
   */
  private void formatStatement() {
    this.statement =
        CommonsUtils.format(
            statement,
            "{column-%s}",
            getColumns().toArray());

    SqlTable table = getTable();
    if (table != null) {
      statement = statement.replace(
          DataType.TABLE.getPlaceholder(),
          table.getName());
    }
  }

  @Override
  public PreparedStatement addParameters(List<?> parameters) {
    if (!CommonsUtils.isNullOrEmpty(parameters)) {
      metaData.addParameters(parameters);
    }
    return this;
  }

  @Override
  public PreparedStatement addParameters(Object parameter) {
    if (parameter != null) {
      metaData.addParameters(parameter);
    }
    return this;
  }

  @Override
  public PreparedStatement addColumns(List<String> columns) {
    if (!CommonsUtils.isNullOrEmpty(columns)) {
      metaData.addColumns(columns);
    }
    return this;
  }

  @Override
  public PreparedStatement addColumns(String column) {
    if (!CommonsUtils.isNullOrEmpty(column)) {
      metaData.addColumns(column);
    }
    return this;
  }

  @Override
  public PreparedStatement setTable(SqlTable table) {
    metaData.setTable(table);
    return this;
  }

  @Override
  public PreparedStatement setMetaData(StatementMetadata metaData) {
    this.metaData.setMetaData(metaData);
    return this;
  }

  @Override
  public StatementBasicData addMetaData(StatementMetadata metaData) {
    this.metaData.addMetaData(metaData);
    return this;
  }

  @Override
  public StatementBasicData setMetaData(@Nullable SqlTable table,
                                        List<String> cols,
                                        List<Object> params) {
    this.metaData = new StatementBasicDataImpl(table, cols, params);
    return this;
  }

  @Override
  public SqlStatementBody getBody() {
    return new SqlStatementBodyPackage(statement);
  }

  @Override
  public StatementBasicData getModifiableData() {
    return metaData;
  }

  @Override
  public String getTableName() {
    SqlTable table = getTable();
    return table != null
        ? table.getName()
        : "";
  }

  @Override
  public String getStatement() {
    return statement;
  }

  @Override
  public List<Object> getParams() {
    List<Object> params = metaData.getParams();
    return CommonsUtils.isNullOrEmpty(params)
        ? Collections.emptyList() : params;
  }

  @Override
  public List<String> getColumns() {
    List<String> columns = metaData.getColumns();
    return CommonsUtils.isNullOrEmpty(columns)
        ? Collections.emptyList() : columns;
  }

  @Override
  public SqlTable getTable() {
    return metaData.getTable();
  }

  @Override
  public String toString() {
    return "SqlPreparedStatementImpl{" +
        "statement='" + statement + '\'' +
        ", metaData=" + metaData.toString() +
        '}';
  }
}
