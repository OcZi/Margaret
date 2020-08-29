package me.oczi.common.storage.sql.orm.dsl.clause.jump;

import me.oczi.common.storage.sql.orm.dsl.clause.LimitPattern;

public interface LimitJumpClause<C> {

  C limit(int limitInt);

  C limit(int limitInt, LimitPattern pattern);
}
