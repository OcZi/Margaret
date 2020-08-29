package me.oczi.common.storage.sql.orm.dsl.clause;

import me.oczi.common.storage.sql.orm.statements.builder.StatementBuilder;

import java.util.Arrays;
import java.util.List;

import static me.oczi.common.storage.sql.orm.dsl.update.UpdateStatementPattern.SET;

public interface SetClause {

  static <C> C set(C clazz,
                   StatementBuilder builder,
                   String column,
                   Object... parameters) {
    return set(clazz,
        builder,
        column,
        Arrays.asList(parameters));
  }

  static <C> C set(C clazz,
                   StatementBuilder builder,
                   String column,
                   List<?> parameters) {
    builder.appendColumn(SET,
        column,
        parameters);
    return clazz;
  }
}
