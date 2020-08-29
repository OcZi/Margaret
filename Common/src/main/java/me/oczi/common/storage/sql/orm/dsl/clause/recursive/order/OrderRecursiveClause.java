package me.oczi.common.storage.sql.orm.dsl.clause.recursive.order;

import me.oczi.common.storage.sql.orm.dsl.clause.OrderClause;
import me.oczi.common.storage.sql.orm.statements.builder.StatementBuilder;
import me.oczi.common.storage.sql.orm.dsl.clause.OrderPattern;
import me.oczi.common.storage.sql.orm.dsl.clause.recursive.AbstractRecursiveClause;

/**
 * Order Recursive Clause class.
 * Add columns Ascending or descending recursively.
 */
public class OrderRecursiveClause
    extends AbstractRecursiveClause
    implements OrderRecursiveStart, OrderRecursiveColumn {

  public OrderRecursiveClause(StatementBuilder statementBuilder) {
    super(statementBuilder);
  }

  @Override
  public OrderRecursiveColumn orderBy(String column,
                                      OrderPattern orderPattern) {
    return OrderClause.orderBy(this,
        statementBuilder,
        column,
        orderPattern);
  }

  @Override
  public OrderRecursiveColumn orderBy(String[] column) {
    return OrderClause.orderBy(this,
        statementBuilder,
        column);
  }

  @Override
  public OrderRecursiveColumn orderBy(String[] column,
                                      OrderPattern orderPattern) {
    return OrderClause.orderBy(this,
        statementBuilder,
        column);
  }

  @Override
  public OrderRecursiveColumn orderBy(String column) {
    return OrderClause.orderBy(this,
        statementBuilder,
        column);
  }

  // Splitter ", " Hardcoded.
  // Will not be added with appendSpace because will always
  // start with ORDER BY clause and create unnecessary spaces.
  @Override
  public OrderRecursiveColumn column(String[] columns) {
    String clause = ", " +
        String.join(", ", columns);
    statementBuilder.append(clause);
    return this;
  }

  // FIXME: Syntax incomplete. Order Pattern only applies to the last column.
  @Override
  public OrderRecursiveColumn column(String[] columns,
                                     OrderPattern orderPattern) {
    String clause = ", " +
        String.join(", ", columns) +
        " " +
        orderPattern.getPattern();
    statementBuilder.append(clause);
    return this;
  }

  @Override
  public OrderRecursiveColumn column(String column) {
    String clause = ", " + column;
    statementBuilder.append(clause);
    return this;
  }

  @Override
  public OrderRecursiveColumn column(String column,
                                     OrderPattern orderPattern) {
    String clause = ", " +
        column +
        " " +
        orderPattern.getPattern();
    statementBuilder.append(clause);
    return this;
  }
}
