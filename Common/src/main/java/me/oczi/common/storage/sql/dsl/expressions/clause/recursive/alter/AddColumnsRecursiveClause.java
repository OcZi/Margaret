package me.oczi.common.storage.sql.dsl.expressions.clause.recursive.alter;

import me.oczi.common.storage.sql.dsl.expressions.clause.AlterClause;
import me.oczi.common.storage.sql.dsl.expressions.clause.recursive.AbstractRecursiveClause;
import me.oczi.common.storage.sql.dsl.statements.builder.StatementBuilder;

public class AddColumnsRecursiveClause
    extends AbstractRecursiveClause
    implements AddColumnsRecursive {

  public AddColumnsRecursiveClause(StatementBuilder statementBuilder) {
    super(statementBuilder);
  }

  @Override
  public AddColumnsRecursive add(String columnName, String dataType, String... constraints) {
    return AlterClause.addColumn(this,
        statementBuilder,
        columnName,
        dataType,
        constraints);
  }

  @Override
  public AddColumnsRecursive add(String column) {
    return AlterClause.addColumn(this,
        statementBuilder,
        column);
  }
}
