package me.oczi.common.storage.sql.orm.dsl.clause.jump;

import me.oczi.common.storage.sql.orm.dsl.clause.OrderPattern;

public interface OrderBasicJumpClause<C> {

  C orderBy(String[] column);

  C orderBy(String[] column, OrderPattern orderPattern);

  C orderBy(String column);

  C orderBy(String column, OrderPattern orderPattern);
}
