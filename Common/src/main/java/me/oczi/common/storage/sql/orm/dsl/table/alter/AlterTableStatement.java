package me.oczi.common.storage.sql.orm.dsl.table.alter;

import me.oczi.common.api.sql.SqlTable;
import me.oczi.common.storage.sql.datasource.DataSourceType;
import me.oczi.common.storage.sql.orm.dsl.clause.AlterClause;
import me.oczi.common.storage.sql.orm.dsl.clause.recursive.alter.AddColumnsRecursive;
import me.oczi.common.storage.sql.orm.statements.AbstractStatement;
import me.oczi.common.storage.sql.orm.statements.prepared.PreparedStatement;

import java.util.function.Function;

import static me.oczi.common.storage.sql.orm.statements.SqlStatementBase.ALTER_TABLE;

public class AlterTableStatement
    extends AbstractStatement
    implements AlterTableClauses{
  private final DataSourceType dataSourceType;

  public AlterTableStatement(SqlTable table, DataSourceType dataSourceType) {
    super(ALTER_TABLE, table);
    this.dataSourceType = dataSourceType;
  }

  /*
  @Override
  public AlterTableClauses addColumns(String columnName, String dataType, String... constraints) {
    return AlterClause.addColumn(this,
        statementBuilder,
        columnName,
        dataType,
        constraints);
  }
  */

  @Override
  public AlterTableClauses addColumn(String column) {
    return AlterClause.addColumn(this,
        statementBuilder,
        column);
  }

  // Hardcoded Postgresql's ADD COLUMN clause.
  @Override
  public AlterTableClauses addColumns(String[] columns) {
    return dataSourceType == DataSourceType.POSTGRESQL
        ? AlterClause.addColumnsPostgres(this,
        statementBuilder,
        columns)
        : AlterClause.addColumns(this,
        statementBuilder,
        columns);
  }

  @Override
  public AlterTableClauses addColumnRecursive(Function<AddColumnsRecursive, AddColumnsRecursive> function) {
    return AlterClause.addColumnsRecursive(this,
        statementBuilder,
        function);
  }

  @Override
  public PreparedStatement build() {
    return statementBuilder.build();
  }
}
