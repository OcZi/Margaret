package me.oczi.common.storage.sql.dsl.expressions.select;

import me.oczi.common.storage.sql.dsl.expressions.clause.jump.LimitJumpClause;
import me.oczi.common.storage.sql.dsl.expressions.clause.jump.OrderJumpClause;
import me.oczi.common.api.state.FinishedState;
import me.oczi.common.storage.sql.dsl.expressions.clause.jump.WhereJumpClause;

public interface SelectClauses
    extends WhereJumpClause<SelectClauses>,
    OrderJumpClause<SelectClauses>,
    LimitJumpClause<SelectClauses>,
    FinishedState {
}
