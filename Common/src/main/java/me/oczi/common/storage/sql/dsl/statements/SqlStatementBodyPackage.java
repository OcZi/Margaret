package me.oczi.common.storage.sql.dsl.statements;

import me.oczi.common.storage.sql.dsl.statements.data.SqlStatementBody;

public class SqlStatementBodyPackage implements SqlStatementBody {
  private final String statement;

  public SqlStatementBodyPackage(String statement) {
    this.statement = statement;
  }

  @Override
  public String getStatement() {
    return statement;
  }
}
