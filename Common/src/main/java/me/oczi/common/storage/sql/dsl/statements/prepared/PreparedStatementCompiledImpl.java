package me.oczi.common.storage.sql.dsl.statements.prepared;

import java.util.Collections;
import java.util.List;

public class PreparedStatementCompiledImpl implements PreparedStatementCompiled {
  private final String statement;
  private final List<Object> params;

  public PreparedStatementCompiledImpl(String statement) {
    this.statement = statement;
    this.params = Collections.emptyList();
  }

  public PreparedStatementCompiledImpl(PreparedStatement preparedStatement) {
    this.statement = preparedStatement.getStatement();
    this.params = preparedStatement.getParams();
  }

  @Override
  public List<Object> getParams() {
    return params;
  }

  @Override
  public String getStatement() {
    return statement;
  }
}
