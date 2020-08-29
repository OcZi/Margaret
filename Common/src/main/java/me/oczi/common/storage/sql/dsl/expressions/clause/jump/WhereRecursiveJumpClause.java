package me.oczi.common.storage.sql.dsl.expressions.clause.jump;

import me.oczi.common.storage.sql.dsl.expressions.clause.recursive.AndOrRecursiveClause;
import me.oczi.common.storage.sql.dsl.expressions.clause.recursive.where.WhereRecursiveStart;

import java.util.function.Function;

public interface WhereRecursiveJumpClause<C> {

  C whereRecursive(Function<WhereRecursiveStart,
      AndOrRecursiveClause> recursive);
}
