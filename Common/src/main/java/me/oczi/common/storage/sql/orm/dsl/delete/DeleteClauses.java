package me.oczi.common.storage.sql.orm.dsl.delete;

import me.oczi.common.storage.sql.orm.dsl.clause.jump.WhereJumpClause;
import me.oczi.common.api.state.FinishedState;

public interface DeleteClauses
    extends WhereJumpClause<DeleteClauses>,
    FinishedState {
}
