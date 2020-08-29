package me.oczi.common.storage.sql.orm.dsl.clause.recursive;

import me.oczi.common.api.state.UnfinishedState;

public interface AndOrRecursiveClause extends UnfinishedState {

  AndOrRecursiveClause and(String column, Object... values);

  AndOrRecursiveClause andNot(String column, Object... values);

  /*
  AndOrRecursiveClause andInverted(Object value, String... columns);

  AndOrRecursiveClause andNotInverted(Object value, String... columns);
  */

  AndOrRecursiveClause or(String column, Object... values);

  AndOrRecursiveClause orNot(String column, Object... values);

  /*
  AndOrRecursiveClause orInverted(Object value, String... columns);

  AndOrRecursiveClause orNotInverted(Object value, String... columns);
  */
}

