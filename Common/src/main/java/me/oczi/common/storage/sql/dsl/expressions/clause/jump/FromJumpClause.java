package me.oczi.common.storage.sql.dsl.expressions.clause.jump;

import me.oczi.common.api.sql.SqlTable;

public interface FromJumpClause<C> {

  C from(SqlTable table);
}
