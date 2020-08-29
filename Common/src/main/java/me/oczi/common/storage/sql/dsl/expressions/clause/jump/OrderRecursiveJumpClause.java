package me.oczi.common.storage.sql.dsl.expressions.clause.jump;

import me.oczi.common.storage.sql.dsl.expressions.clause.recursive.order.OrderRecursiveColumn;
import me.oczi.common.storage.sql.dsl.expressions.clause.recursive.order.OrderRecursiveStart;

import java.util.function.Function;

public interface OrderRecursiveJumpClause<C> {

  C orderRecursive(Function<OrderRecursiveStart,
      OrderRecursiveColumn> recursive);
}
