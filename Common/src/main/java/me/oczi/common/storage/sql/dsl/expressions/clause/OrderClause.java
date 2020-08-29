package me.oczi.common.storage.sql.dsl.expressions.clause;

import me.oczi.common.storage.sql.dsl.statements.builder.StatementBuilder;
import me.oczi.common.storage.sql.dsl.expressions.clause.recursive.order.OrderRecursiveClause;
import me.oczi.common.storage.sql.dsl.expressions.clause.recursive.order.OrderRecursiveColumn;
import me.oczi.common.storage.sql.dsl.expressions.clause.recursive.order.OrderRecursiveStart;

import java.util.function.Function;

import static me.oczi.common.storage.sql.dsl.other.ClauseStatementPattern.ORDER;

public interface OrderClause {

  static <C> C orderBy(C clazz,
                       StatementBuilder builder,
                       String columns,
                       OrderPattern orderPattern) {
    builder.appendColumn(ORDER, columns)
        .appendSpace(orderPattern.getPattern());
    return clazz;
  }

  static <C> C orderBy(C clazz,
                       StatementBuilder builder,
                       String... columns) {
    builder.appendColumn(ORDER, columns);
    return clazz;
  }

  static <C> C orderBy(C clazz,
                       StatementBuilder builder,
                       String[] columns,
                       OrderPattern orderPattern) {
    builder.appendColumn(ORDER, columns)
           .appendSpace(orderPattern.getPattern());
    return clazz;
  }

  static <C> C orderBy(C clazz,
                       StatementBuilder builder,
                       Function<OrderRecursiveStart,
                           OrderRecursiveColumn> function) {
    StatementBuilder recursiveClause =
        ((OrderRecursiveClause) function
            .apply(new OrderRecursiveClause(builder)))
            .build();
    builder.appendStatementBuilder(recursiveClause);
    return clazz;
  }
}
