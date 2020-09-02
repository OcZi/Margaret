package me.oczi.common.storage.sql.dsl.expressions.clause;

import me.oczi.common.storage.sql.dsl.statements.builder.StatementBuilder;

import static me.oczi.common.storage.sql.dsl.other.ClauseStatementPattern.OFFSET;

public interface OffsetClause {

  static <C> C offset(C clazz,
                      StatementBuilder builder,
                      int offsetInt) {
    builder.appendPlain(OFFSET, offsetInt);
    return clazz;
  }
}
