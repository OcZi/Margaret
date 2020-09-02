package me.oczi.common.storage.sql.dsl.expressions.clause.jump;

public interface OffsetJumpClause<C> {

  C offset(int offset);
}
