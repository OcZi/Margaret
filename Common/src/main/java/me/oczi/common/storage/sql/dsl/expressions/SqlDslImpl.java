package me.oczi.common.storage.sql.dsl.expressions;

import me.oczi.common.api.sql.SqlTable;
import me.oczi.common.storage.sql.datasource.DataSourceType;
import me.oczi.common.storage.sql.dsl.expressions.delete.DeleteClauses;
import me.oczi.common.storage.sql.dsl.expressions.delete.DeleteStatement;
import me.oczi.common.storage.sql.dsl.expressions.insert.InsertClauses;
import me.oczi.common.storage.sql.dsl.expressions.insert.InsertStatement;
import me.oczi.common.storage.sql.dsl.expressions.select.SelectStart;
import me.oczi.common.storage.sql.dsl.expressions.select.SelectStatement;
import me.oczi.common.storage.sql.dsl.expressions.select.SelectStatementFunction;
import me.oczi.common.storage.sql.dsl.expressions.table.alter.AlterTableStart;
import me.oczi.common.storage.sql.dsl.expressions.table.alter.AlterTableStatement;
import me.oczi.common.storage.sql.dsl.expressions.table.builder.TableBuilderImpl;
import me.oczi.common.storage.sql.dsl.expressions.table.builder.TableBuilderStart;
import me.oczi.common.storage.sql.dsl.expressions.update.UpdateStart;
import me.oczi.common.storage.sql.dsl.expressions.update.UpdateStatement;

public class SqlDslImpl implements SqlDsl {
  private final DataSourceType dataSourceType;

  public SqlDslImpl(DataSourceType dataSourceType) {
    this.dataSourceType = dataSourceType;
  }

  @Override
  public TableBuilderStart createTable(SqlTable table) {
    return new TableBuilderImpl(table, dataSourceType);
  }

  @Override
  public TableBuilderStart createTable(SqlTable table, DataSourceType dataSourceType) {
    return new TableBuilderImpl(table, dataSourceType);
  }

  @Override
  public AlterTableStart alterTable(SqlTable table) {
    return new AlterTableStatement(table, dataSourceType);
  }

  @Override
  public InsertClauses insert(DataSourceType dataSourceType) {
    return new InsertStatement(dataSourceType);
  }

  @Override
  public InsertClauses insert() {
    return new InsertStatement(dataSourceType);
  }

  @Override
  public SelectStart select(String... columns) {
    return new SelectStatement(columns);
  }

  @Override
  public SelectStart select(Object... columns) {
    return new SelectStatement(columns);
  }

  @Override
  public SelectStart select(SelectStatementFunction statementPattern) {
    return new SelectStatement(statementPattern);
  }

  @Override
  public DeleteClauses deleteFrom(SqlTable table) {
    return new DeleteStatement(table);
  }

  @Override
  public UpdateStart update(SqlTable table) {
    return new UpdateStatement(table);
  }
}
