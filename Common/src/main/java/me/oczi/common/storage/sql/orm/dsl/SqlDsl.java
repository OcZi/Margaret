package me.oczi.common.storage.sql.orm.dsl;

import me.oczi.common.api.sql.SqlTable;
import me.oczi.common.storage.sql.datasource.DataSourceType;
import me.oczi.common.storage.sql.orm.dsl.delete.DeleteClauses;
import me.oczi.common.storage.sql.orm.dsl.insert.InsertClauses;
import me.oczi.common.storage.sql.orm.dsl.select.SelectStart;
import me.oczi.common.storage.sql.orm.dsl.select.SelectStatementFunction;
import me.oczi.common.storage.sql.orm.dsl.table.alter.AlterTableStart;
import me.oczi.common.storage.sql.orm.dsl.table.builder.TableBuilderStart;
import me.oczi.common.storage.sql.orm.dsl.update.UpdateStart;

public interface SqlDsl {

  TableBuilderStart createTable(SqlTable table);

  TableBuilderStart createTable(SqlTable table, DataSourceType dataSourceType);

  AlterTableStart alterTable(SqlTable table);

  InsertClauses insert();

  SelectStart select(String... columns);

  SelectStart select(Object... columns);

  SelectStart select(SelectStatementFunction statementPattern);

  DeleteClauses deleteFrom(SqlTable table);

  UpdateStart update(SqlTable table);
}
