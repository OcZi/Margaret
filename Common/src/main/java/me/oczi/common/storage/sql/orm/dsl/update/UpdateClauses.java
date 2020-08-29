package me.oczi.common.storage.sql.orm.dsl.update;

import me.oczi.common.storage.sql.orm.dsl.clause.jump.WhereJumpClause;
import me.oczi.common.api.state.FinishedState;

public interface UpdateClauses
    extends WhereJumpClause<UpdateClauses>,
    FinishedState {
}
