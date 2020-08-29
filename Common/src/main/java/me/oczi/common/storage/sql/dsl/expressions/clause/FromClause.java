package me.oczi.common.storage.sql.dsl.expressions.clause;

import me.oczi.common.api.sql.SqlTable;
import me.oczi.common.storage.sql.dsl.statements.builder.StatementBuilder;

import static me.oczi.common.storage.sql.dsl.other.ClauseStatementPattern.FROM;

public interface FromClause {

  static <C> C from(C clazz,
                    StatementBuilder builder,
                    SqlTable table) {
    builder.appendAndSetTable(FROM, table);
    return clazz;
  }
}
