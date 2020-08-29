package me.oczi.common.storage.sql.dsl.expressions.clause.jump;

import me.oczi.common.storage.sql.dsl.expressions.clause.LimitPattern;

public interface LimitJumpClause<C> {

  C limit(int limitInt);

  C limit(int limitInt, LimitPattern pattern);
}
