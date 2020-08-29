package me.oczi.common.storage.sql.dsl.expressions.clause.recursive.where;

import com.google.common.collect.Lists;
import me.oczi.common.api.sql.StatementPattern;
import me.oczi.common.storage.sql.dsl.expressions.clause.WhereClause;
import me.oczi.common.storage.sql.dsl.expressions.clause.recursive.AbstractRecursiveClause;
import me.oczi.common.storage.sql.dsl.expressions.clause.recursive.AndOrRecursiveClause;
import me.oczi.common.storage.sql.dsl.statements.builder.StatementBuilder;

import java.util.Arrays;
import java.util.List;

import static me.oczi.common.storage.sql.dsl.other.LogicalOperatorPattern.*;

public class WhereRecursiveClause extends AbstractRecursiveClause
    implements WhereRecursiveStart,
    AndOrRecursiveClause {

  public WhereRecursiveClause(StatementBuilder statementBuilder) {
    super(statementBuilder);
  }

  @Override
  public AndOrRecursiveClause where(String column,
                                    Object... parameters) {
    return WhereClause.where(this,
        statementBuilder,
        column,
        parameters);
  }

  @Override
  public AndOrRecursiveClause where(String column, List<?> parameters) {
    return WhereClause.where(this,
        statementBuilder,
        column,
        parameters);
  }

  @Override
  public AndOrRecursiveClause whereNot(String column, Object... parameters) {
    return WhereClause.whereNot(this,
        statementBuilder,
        column,
        parameters);
  }

  @Override
  public AndOrRecursiveClause whereNot(String column, List<?> parameters) {
    return WhereClause.whereNot(this,
        statementBuilder,
        column,
        parameters);
  }

  /*
  @Override
  public AndOrRecursiveClause whereInverted(Object value, String... columns) {
    return WhereClause.whereInverted(this,
        statementBuilder,
        value,
        columns);
  }

  @Override
  public AndOrRecursiveClause whereNotInverted(Object value, String... columns) {
    return WhereClause.whereNotInverted(this,
        statementBuilder,
        value,
        columns);
  }
  */

  @Override
  public AndOrRecursiveClause and(String column, Object... values) {
    statementBuilder.appendColumn(AND,
        column,
        Arrays.asList(values));
    return this;
  }

  @Override
  public AndOrRecursiveClause andNot(String column, Object... values) {
    List<StatementPattern> patterns = Lists.newArrayList(AND, NOT);
    statementBuilder.appendColumn(patterns,
        column,
        Arrays.asList(values));
    return this;
  }

  /*
  @Override
  public AndOrRecursiveClause andInverted(Object value,
                                          String... columns) {
    statementBuilder.appendClause(AND,
        Lists.newArrayList(DataType.PARAMETER),
        Arrays.asList(columns),
        Collections.singletonList(value));
    return this;
  }

  @Override
  public AndOrRecursiveClause andNotInverted(Object value, String... columns) {
    List<StatementPattern> patterns = Lists.newArrayList(AND, NOT);
    statementBuilder.appendClause(patterns,
        Lists.newArrayList(DataType.PARAMETER),
        Arrays.asList(columns),
        Collections.singletonList(value));
    return this;
  }
  */

  @Override
  public AndOrRecursiveClause or(String column,
                                 Object... values) {
    statementBuilder.appendColumn(OR,
        column,
        Arrays.asList(values));
    return this;
  }

  @Override
  public AndOrRecursiveClause orNot(String column, Object... values) {
    List<StatementPattern> patterns = Lists.newArrayList(OR, NOT);
    statementBuilder.appendColumn(patterns,
        column,
        Arrays.asList(values));
    return this;
  }

  /*
  @Override
  public AndOrRecursiveClause orInverted(Object value,
                                         String... columns) {
    statementBuilder.appendClause(OR,
        Lists.newArrayList(DataType.PARAMETER),
        Arrays.asList(columns),
        Collections.singletonList(value));
    return this;
  }

  @Override
  public AndOrRecursiveClause orNotInverted(Object value, String... columns) {
    List<StatementPattern> patterns = Lists.newArrayList(OR, NOT);
    statementBuilder.appendClause(patterns,
        Lists.newArrayList(DataType.PARAMETER),
        Arrays.asList(columns),
        Collections.singletonList(value));
    return this;
  }
  */
}
