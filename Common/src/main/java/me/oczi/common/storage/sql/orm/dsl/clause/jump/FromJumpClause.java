package me.oczi.common.storage.sql.orm.dsl.clause.jump;

import me.oczi.common.api.sql.SqlTable;

public interface FromJumpClause<C> {

  C from(SqlTable table);
}
