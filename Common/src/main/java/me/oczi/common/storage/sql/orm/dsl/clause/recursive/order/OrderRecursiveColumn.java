package me.oczi.common.storage.sql.orm.dsl.clause.recursive.order;

import me.oczi.common.storage.sql.orm.dsl.clause.OrderPattern;

public interface OrderRecursiveColumn {

  OrderRecursiveColumn column(String[] columns);

  OrderRecursiveColumn column(String[] columns, OrderPattern orderPattern);

  OrderRecursiveColumn column(String column);

  OrderRecursiveColumn column(String column, OrderPattern orderPattern);
}
