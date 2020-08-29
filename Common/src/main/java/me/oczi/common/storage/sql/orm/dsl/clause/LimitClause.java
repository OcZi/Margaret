package me.oczi.common.storage.sql.orm.dsl.clause;

import me.oczi.common.storage.sql.orm.statements.builder.StatementBuilder;

import static me.oczi.common.storage.sql.orm.other.ClauseStatementPattern.LIMIT;

public interface LimitClause {

  static <C> C limit(C clazz,
                     StatementBuilder builder,
                     int limitInt) {
    builder.appendPlain(LIMIT, limitInt);
    return clazz;
  }

  static <C> C limit(C clazz,
                     StatementBuilder builder,
                     int limitInt,
                     LimitPattern pattern) {
    builder.appendPlain(LIMIT, limitInt, pattern.getPattern());
    return clazz;
  }
}
