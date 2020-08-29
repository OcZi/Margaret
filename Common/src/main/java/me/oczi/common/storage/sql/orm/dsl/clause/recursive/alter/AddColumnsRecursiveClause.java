package me.oczi.common.storage.sql.orm.dsl.clause.recursive.alter;

import me.oczi.common.storage.sql.orm.dsl.clause.AlterClause;
import me.oczi.common.storage.sql.orm.dsl.clause.recursive.AbstractRecursiveClause;
import me.oczi.common.storage.sql.orm.statements.builder.StatementBuilder;

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
