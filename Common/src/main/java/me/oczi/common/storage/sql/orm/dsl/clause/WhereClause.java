package me.oczi.common.storage.sql.orm.dsl.clause;

import com.google.common.collect.Lists;
import me.oczi.common.api.sql.StatementPattern;
import me.oczi.common.storage.sql.orm.dsl.clause.recursive.AndOrRecursiveClause;
import me.oczi.common.storage.sql.orm.dsl.clause.recursive.where.WhereRecursiveClause;
import me.oczi.common.storage.sql.orm.dsl.clause.recursive.where.WhereRecursiveStart;
import me.oczi.common.storage.sql.orm.statements.builder.StatementBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static me.oczi.common.storage.sql.orm.other.ClauseStatementPattern.WHERE;
import static me.oczi.common.storage.sql.orm.other.LogicalOperatorPattern.NOT;

public interface WhereClause {

  static <C> C where(C clazz,
                     StatementBuilder builder,
                     String column,
                     Object parameter) {
    return where(clazz,
        builder,
        column,
        Collections.singletonList(parameter));
  }

  static <C> C where(C clazz,
                     StatementBuilder builder,
                     String column,
                     Object... parameters) {
    return where(clazz,
        builder,
        column,
        Arrays.asList(parameters));
  }

  static <C> C where(C clazz,
                     StatementBuilder builder,
                     String column,
                     List<?> parameters) {
    builder.appendColumn(WHERE,
        column,
        parameters);
    return clazz;
  }

  /*
  static <C> C whereInverted(C clazz,
                             StatementBuilder builder,
                             String column,
                             Object parameter) {
    return whereInverted(clazz, builder, parameter, column);
  }

  static <C> C whereInverted(C clazz,
                             StatementBuilder builder,
                             Object parameters,
                             String... column) {
    builder.appendClause(WHERE,
        Lists.newArrayList(DataType.PARAMETER),
        Arrays.asList(column),
        Collections.singletonList(parameters));
    return clazz;
  }
  */

  static <C> C whereNot(C clazz,
                        StatementBuilder builder,
                        String column,
                        Object parameters) {
    return whereNot(clazz, builder, column, new Object[]{parameters});
  }

  static <C> C whereNot(C clazz,
                        StatementBuilder builder,
                        String column,
                        Object... parameters) {
    List<StatementPattern> patterns = Lists.newArrayList(WHERE, NOT);
    builder.appendColumn(patterns,
        column,
        Arrays.asList(parameters));
    return clazz;
  }

  /*
  static <C> C whereNotInverted(C clazz,
                                StatementBuilder builder,
                                String column,
                                Object parameter) {
    return whereNotInverted(clazz, builder, parameter, column);
  }

  static <C> C whereNotInverted(C clazz,
                                StatementBuilder builder,
                                Object parameter,
                                String... columns) {
    List<StatementPattern> patterns = Lists.newArrayList(WHERE, NOT);
    builder.appendClause(patterns,
        Lists.newArrayList(DataType.PARAMETER),
        Arrays.asList(columns),
        Collections.singletonList(parameter));
    return clazz;
  }
  */

  static <C> C whereRecursive(C clazz,
                              StatementBuilder builder,
                              Function<WhereRecursiveStart,
                                  AndOrRecursiveClause> function) {
    StatementBuilder clauseBuilder = function
        .apply(new WhereRecursiveClause(builder))
        .build();
    builder.appendStatementBuilder(clauseBuilder);
    return clazz;
  }
}
