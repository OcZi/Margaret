package me.oczi.common.storage.sql.dsl.statements.data;

import me.oczi.common.api.sql.SqlTable;
import me.oczi.common.utils.CommonsUtils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class StatementBasicDataImpl
    implements StatementBasicData {
  private SqlTable table;
  private List<Object> params = new ArrayList<>();
  private List<String> cols;

  public StatementBasicDataImpl(List<String> cols,
                                List<Object> params) {
    this(null, cols, params);
  }

  public StatementBasicDataImpl(@Nullable SqlTable table,
                                List<String> cols,
                                List<?> params) {
    if (table != null) {
      this.table = table;
    }
    if (!CommonsUtils.isNullOrEmpty(params)) {
      this.params.addAll(params);
    }
    this.cols = cols;
  }

  public StatementBasicDataImpl(StatementMetadata metadata) {
    this.table = metadata.getTable();
    this.params = metadata.getParams();
    this.cols = metadata.getColumns();
  }

  @Override
  public StatementBasicData addParameters(List<?> parameters) {
    if (!parameters.isEmpty()) {
      params.addAll(parameters);
    }
    return this;
  }

  @Override
  public StatementBasicData addParameters(Object parameter) {
    if (parameter != null) {
      params.add(parameter);
    }
    return this;
  }

  @Override
  public StatementBasicData addColumns(List<String> column) {
    if (!column.isEmpty()) {
      cols.addAll(column);
    }
    return this;
  }

  @Override
  public StatementBasicData addColumns(String column) {
    if (!column.isEmpty()) {
      cols.add(column);
    }
    return this;
  }

  @Override
  public StatementBasicData setTable(SqlTable table) {
    this.table = table;
    return this;
  }

  @Override
  public StatementBasicData setMetaData(@Nullable SqlTable table,
                                        List<String> cols,
                                        List<Object> params) {
    this.table = table;
    this.cols = cols;
    this.params = params;
    return this;
  }

  @Override
  public SqlStatementModifiable setMetaData(StatementMetadata metaData) {
    this.cols = metaData.getColumns();
    this.params = metaData.getParams();
    SqlTable table = metaData.getTable();
    if (table != null) {
      this.table = table;
    }
    return this;
  }

  @Override
  public SqlStatementModifiable addMetaData(StatementMetadata metaData) {
    this.cols.addAll(metaData.getColumns());
    this.params.addAll(metaData.getParams());
    return this;
  }

  @Override
  public SqlStatementModifiable getModifiableData() {
    return this;
  }

  @Override
  public List<Object> getParams() {
    return params;
  }

  @Override
  public List<String> getColumns() {
    return cols;
  }

  @Override
  public SqlTable getTable() {
    return table;
  }

  @Override
  public String toString() {
    return "SqlStatementBasicData{" +
        "table=" + table +
        ", params=" + params +
        ", columns=" + cols +
        '}';
  }
}
