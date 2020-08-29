package me.oczi.common.storage.sql.orm.dsl.update;

import me.oczi.common.api.sql.SqlTable;
import me.oczi.common.storage.sql.orm.statements.AbstractStatement;
import me.oczi.common.storage.sql.orm.dsl.clause.SetClause;
import me.oczi.common.storage.sql.orm.dsl.clause.WhereClause;
import me.oczi.common.storage.sql.orm.dsl.clause.recursive.AndOrRecursiveClause;
import me.oczi.common.storage.sql.orm.dsl.clause.recursive.where.WhereRecursiveStart;
import me.oczi.common.storage.sql.orm.statements.prepared.PreparedStatement;

import java.util.List;
import java.util.function.Function;

import static me.oczi.common.storage.sql.orm.statements.SqlStatementBase.UPDATE;

public class UpdateStatement extends AbstractStatement
    implements UpdateClauses, UpdateStart{

  public UpdateStatement(SqlTable table) {
    super(UPDATE, table);
  }

  @Override
  public UpdateClauses set(String column,
                           Object... parameters) {
    return SetClause.set(this,
        statementBuilder,
        column,
        parameters);
  }

  @Override
  public UpdateClauses set(String column,
                           List<?> parameters) {
    return SetClause.set(this,
        statementBuilder,
        column,
        parameters);
  }

  @Override
  public UpdateClauses where(String column,
                             Object... parameters) {
    return WhereClause.where(this,
        statementBuilder,
        column,
        parameters);
  }

  @Override
  public UpdateClauses where(String column, List<?> parameters) {
    return WhereClause.where(this,
        statementBuilder,
        column,
        parameters);
  }

  @Override
  public UpdateClauses whereNot(String column, Object... parameters) {
    return WhereClause.whereNot(this,
        statementBuilder,
        column,
        parameters);
  }

  @Override
  public UpdateClauses whereNot(String column, List<?> parameters) {
    return WhereClause.whereNot(this,
        statementBuilder,
        column,
        parameters);
  }

  @Override
  public UpdateClauses whereRecursive(Function<WhereRecursiveStart, AndOrRecursiveClause> recursive) {
    return WhereClause.whereRecursive(this,
        statementBuilder,
        recursive);
  }

  @Override
  public PreparedStatement build() {
    return statementBuilder.build();
  }
}
