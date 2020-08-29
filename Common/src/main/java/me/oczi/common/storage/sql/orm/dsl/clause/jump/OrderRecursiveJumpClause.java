package me.oczi.common.storage.sql.orm.dsl.clause.jump;

import me.oczi.common.storage.sql.orm.dsl.clause.recursive.order.OrderRecursiveColumn;
import me.oczi.common.storage.sql.orm.dsl.clause.recursive.order.OrderRecursiveStart;

import java.util.function.Function;

public interface OrderRecursiveJumpClause<C> {

  C orderRecursive(Function<OrderRecursiveStart,
      OrderRecursiveColumn> recursive);
}
