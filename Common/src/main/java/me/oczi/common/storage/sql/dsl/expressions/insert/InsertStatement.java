package me.oczi.common.storage.sql.dsl.expressions.insert;

import me.oczi.common.api.sql.SqlTable;
import me.oczi.common.api.state.FinishedState;
import me.oczi.common.storage.sql.datasource.DataSourceType;
import me.oczi.common.storage.sql.dsl.expressions.clause.ValuesClause;
import me.oczi.common.storage.sql.dsl.statements.AbstractStatement;
import me.oczi.common.storage.sql.dsl.statements.prepared.PreparedStatement;
import me.oczi.common.utils.Statements;

import java.util.List;

import static me.oczi.common.storage.sql.dsl.expressions.insert.InsertStatementPattern.*;
import static me.oczi.common.storage.sql.dsl.statements.SqlStatementBase.INSERT;

public class InsertStatement extends AbstractStatement
    implements InsertClauses, InsertValuesClause {
  private final DataSourceType dataSourceType;

  public InsertStatement(DataSourceType dataSourceType) {
    this.dataSourceType = dataSourceType;
    this.retrievedBase = INSERT.getPattern();
  }

  @Override
  public InsertIntoClause orReplace() {
    switch (dataSourceType) {
      case MYSQL:
        this.retrievedBase = REPLACE.getPattern();
        return this;
      case POSTGRESQL:
        this.retrievedBase = ON_CONFLICT_DO_UPDATE.getPattern();
        return this;
      case H2:
        this.retrievedBase = MERGE.getPattern();
        return this;
      default:
        this.retrievedBase = String.format(
            retrievedBase, OR_REPLACE.getPattern());
        return this;
    }
  }

  @Override
  public InsertValuesClause into(SqlTable table) {
    if (dataSourceType != DataSourceType.POSTGRESQL) {
      this.retrievedBase = String.format(
          retrievedBase, INTO.getPattern());
      statementBuilder.appendAndSetTable(retrievedBase, table);
      return this;
    }

    String pattern = Statements
        .formatPatterns(INSERT, INTO);
    statementBuilder.appendAndSetTable(pattern, table);
    return this;
  }

  @Override
  public FinishedState values(Object... values) {
    ValuesClause.insertValues(statementBuilder, values);
    if (dataSourceType == DataSourceType.POSTGRESQL) {
      ValuesClause.onConflictDoUpdate(statementBuilder, values);
    }
    return this;
  }

  @Override
  public FinishedState values(List<?> values) {
    ValuesClause.insertValues(statementBuilder, values);
    if (dataSourceType == DataSourceType.POSTGRESQL) {
      ValuesClause.onConflictDoUpdate(statementBuilder, values);
    }
    return this;
  }

  @Override
  public PreparedStatement build() {
    return statementBuilder.build();
  }
}
