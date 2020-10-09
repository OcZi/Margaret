package me.oczi.common.storage.sql.dsl.expressions.delete;

import me.oczi.common.storage.sql.dsl.expressions.clause.jump.WhereJumpClause;
import me.oczi.common.api.sql.FinishedState;

public interface DeleteClauses
    extends WhereJumpClause<DeleteClauses>,
    FinishedState {
}
