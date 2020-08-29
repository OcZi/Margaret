package me.oczi.common.storage.sql.orm.statements.prepared;

import java.util.Collections;
import java.util.List;

public class SqlPreparedStatementCompiledImpl implements SqlPreparedStatementCompiled {
  private final String statement;
  private final List<Object> params;

  public SqlPreparedStatementCompiledImpl(String statement) {
    this.statement = statement;
    this.params = Collections.emptyList();
  }

  public SqlPreparedStatementCompiledImpl(PreparedStatement preparedStatement) {
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
