package me.oczi.common.storage.sql.dsl.expressions.delete;

import me.oczi.common.api.sql.SqlTable;
import me.oczi.common.storage.sql.dsl.statements.AbstractStatement;
import me.oczi.common.storage.sql.dsl.expressions.clause.WhereClause;
import me.oczi.common.storage.sql.dsl.expressions.clause.recursive.AndOrRecursiveClause;
import me.oczi.common.storage.sql.dsl.expressions.clause.recursive.where.WhereRecursiveStart;
import me.oczi.common.storage.sql.dsl.statements.prepared.PreparedStatement;

import java.util.List;
import java.util.function.Function;

import static me.oczi.common.storage.sql.dsl.statements.SqlStatementBase.DELETE;

public class DeleteStatement extends AbstractStatement
    implements DeleteClauses {

  public DeleteStatement(SqlTable table) {
    super(DELETE, table);
  }

  @Override
  public DeleteClauses where(String column,
                             Object... parameters) {
    return WhereClause.where(this,
        statementBuilder,
        column,
        parameters);
  }

  @Override
  public DeleteClauses where(String column, List<?> parameters) {
    return WhereClause.where(this,
        statementBuilder,
        column,
        parameters);
  }

  @Override
  public DeleteClauses whereNot(String column,
                                Object... parameters) {
    return WhereClause.whereNot(this,
        statementBuilder,
        column,
        parameters);
  }

  @Override
  public DeleteClauses whereNot(String column, List<?> parameters) {
    return WhereClause.whereNot(this,
        statementBuilder,
        column,
        parameters);
  }

  /*
  @Override
  public DeleteClauses whereInverted(Object value, String... columns) {
    return null;
  }

  @Override
  public DeleteClauses whereNotInverted(Object value, String... columns) {
    return null;
  }
  */

  @Override
  public DeleteClauses whereRecursive(Function<WhereRecursiveStart,
      AndOrRecursiveClause> recursive) {
    return WhereClause.whereRecursive(this,
        statementBuilder,
        recursive);
  }

  @Override
  public PreparedStatement build() {
    return statementBuilder.build();
  }
}
