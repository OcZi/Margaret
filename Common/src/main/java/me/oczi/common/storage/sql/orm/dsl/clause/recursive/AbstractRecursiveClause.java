package me.oczi.common.storage.sql.orm.dsl.clause.recursive;

import me.oczi.common.api.state.UnfinishedState;
import me.oczi.common.storage.sql.orm.statements.builder.StatementBuilder;
import me.oczi.common.storage.sql.orm.statements.builder.StatementBuilderImpl;

public class AbstractRecursiveClause
    implements UnfinishedState {
  protected final StatementBuilder statementBuilder;

  public AbstractRecursiveClause(StatementBuilder statementBuilder) {
    this.statementBuilder = new StatementBuilderImpl(statementBuilder.getNumColumn());
  }

  @Override
  public StatementBuilder build() {
    return statementBuilder;
  }

  @Override
  public String toString() {
    return statementBuilder.toString();
  }
}
