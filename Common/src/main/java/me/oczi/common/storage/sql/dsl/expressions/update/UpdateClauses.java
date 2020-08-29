package me.oczi.common.storage.sql.dsl.expressions.update;

import me.oczi.common.storage.sql.dsl.expressions.clause.jump.WhereJumpClause;
import me.oczi.common.api.state.FinishedState;

public interface UpdateClauses
    extends WhereJumpClause<UpdateClauses>,
    FinishedState {
}
