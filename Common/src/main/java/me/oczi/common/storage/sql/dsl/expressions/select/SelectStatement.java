package me.oczi.common.storage.sql.dsl.expressions.select;

import me.oczi.common.api.sql.SqlTable;
import me.oczi.common.storage.sql.dsl.statements.AbstractStatement;
import me.oczi.common.storage.sql.dsl.expressions.clause.*;
import me.oczi.common.storage.sql.dsl.expressions.clause.recursive.AndOrRecursiveClause;
import me.oczi.common.storage.sql.dsl.expressions.clause.recursive.order.OrderRecursiveColumn;
import me.oczi.common.storage.sql.dsl.expressions.clause.recursive.order.OrderRecursiveStart;
import me.oczi.common.storage.sql.dsl.expressions.clause.recursive.where.WhereRecursiveStart;
import me.oczi.common.storage.sql.dsl.statements.prepared.PreparedStatement;
import me.oczi.common.utils.CommonsUtils;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static me.oczi.common.storage.sql.dsl.statements.SqlStatementBase.SELECT;

public class SelectStatement
    extends AbstractStatement
    implements SelectClauses, SelectStart{

  public SelectStatement(String... columns) {
    super(SELECT, columns);
  }

  public SelectStatement(Object... plain) {
    super(SELECT, CommonsUtils.joinCollection(
        Arrays.asList(plain)));
  }

  public SelectStatement(SelectStatementFunction pattern) {
    super(SELECT, pattern.getPattern());
  }

  @Override
  public SelectClauses from(SqlTable table) {
    return FromClause.from(this, statementBuilder, table);
  }

  @Override
  public SelectClauses where(String column, Object... parameters) {
    return WhereClause.where(this,
        statementBuilder,
        column,
        parameters);
  }

  @Override
  public SelectClauses where(String column, List<?> parameters) {
    return WhereClause.where(this,
        statementBuilder,
        column,
        parameters);
  }

  @Override
  public SelectClauses whereNot(String column, Object... parameters) {
    return WhereClause.whereNot(this,
        statementBuilder,
        column,
        parameters);
  }

  @Override
  public SelectClauses whereNot(String column, List<?> parameters) {
    return WhereClause.whereNot(this,
        statementBuilder,
        column,
        parameters);
  }

  /*
  @Override
  public SelectClauses whereInverted(Object value, String... columns) {
    return null;
  }

  @Override
  public SelectClauses whereNotInverted(Object value, String... columns) {
    return null;
  }
  */

  @Override
  public SelectClauses orderBy(String[] column, OrderPattern orderPattern) {
    return OrderClause.orderBy(this,
        statementBuilder,
        column,
        orderPattern);
  }

  @Override
  public SelectClauses orderBy(String column) {
    return OrderClause.orderBy(this,
        statementBuilder,
        column);
  }

  @Override
  public SelectClauses orderBy(String[] column) {
    return OrderClause.orderBy(this,
        statementBuilder,
        column);
  }

  @Override
  public SelectClauses orderBy(String column, OrderPattern orderPattern) {
    return OrderClause.orderBy(this,
        statementBuilder,
        column,
        orderPattern);
  }

  @Override
  public SelectClauses orderRecursive(Function<OrderRecursiveStart, OrderRecursiveColumn> recursive) {
    return OrderClause.orderBy(this,
        statementBuilder,
        recursive);
  }

  @Override
  public SelectClauses whereRecursive(Function<WhereRecursiveStart,
      AndOrRecursiveClause> recursive) {
    return WhereClause.whereRecursive(this,
        statementBuilder,
        recursive);
  }

  @Override
  public SelectClauses limit(int limitInt) {
    return LimitClause.limit(this,
        statementBuilder,
        limitInt);
  }

  @Override
  public SelectClauses limit(int limitInt, LimitPattern pattern) {
    return LimitClause.limit(this,
        statementBuilder,
        limitInt,
        pattern);
  }

  @Override
  public PreparedStatement build() {
    return statementBuilder.build();
  }

  @Override
  public SelectClauses offset(int offset) {
    return OffsetClause.offset(this,
        statementBuilder,
        offset);
  }
}
