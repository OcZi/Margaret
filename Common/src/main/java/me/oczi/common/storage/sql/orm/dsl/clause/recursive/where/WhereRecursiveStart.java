package me.oczi.common.storage.sql.orm.dsl.clause.recursive.where;

import me.oczi.common.storage.sql.orm.dsl.clause.jump.WhereBasicJumpClause;
import me.oczi.common.storage.sql.orm.dsl.clause.recursive.AndOrRecursiveClause;

public interface WhereRecursiveStart
    extends WhereBasicJumpClause<AndOrRecursiveClause> {
}
