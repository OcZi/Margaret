package me.oczi.common.storage.sql.dsl.expressions;

import me.oczi.common.api.sql.SqlTable;
import me.oczi.common.storage.sql.datasource.DataSourceType;
import me.oczi.common.storage.sql.dsl.expressions.delete.DeleteClauses;
import me.oczi.common.storage.sql.dsl.expressions.insert.InsertClauses;
import me.oczi.common.storage.sql.dsl.expressions.select.SelectStart;
import me.oczi.common.storage.sql.dsl.expressions.select.SelectStatementFunction;
import me.oczi.common.storage.sql.dsl.expressions.table.alter.AlterTableStart;
import me.oczi.common.storage.sql.dsl.expressions.table.builder.TableBuilderStart;
import me.oczi.common.storage.sql.dsl.expressions.update.UpdateStart;

public interface SqlDsl {

  TableBuilderStart createTable(SqlTable table);

  TableBuilderStart createTable(SqlTable table, DataSourceType dataSourceType);

  AlterTableStart alterTable(SqlTable table);

  InsertClauses insert(DataSourceType dataSourceType);

  InsertClauses insert();

  SelectStart select(String... columns);

  SelectStart select(Object... columns);

  SelectStart select(SelectStatementFunction statementPattern);

  DeleteClauses deleteFrom(SqlTable table);

  UpdateStart update(SqlTable table);
}
