package me.oczi.common.storage.sql.dsl.expressions.select;

import me.oczi.common.storage.sql.dsl.expressions.clause.jump.LimitJumpClause;
import me.oczi.common.storage.sql.dsl.expressions.clause.jump.OffsetJumpClause;
import me.oczi.common.storage.sql.dsl.expressions.clause.jump.OrderJumpClause;
import me.oczi.common.api.sql.FinishedState;
import me.oczi.common.storage.sql.dsl.expressions.clause.jump.WhereJumpClause;

public interface SelectClauses
    extends WhereJumpClause<SelectClauses>,
    OrderJumpClause<SelectClauses>,
    LimitJumpClause<SelectClauses>,
    OffsetJumpClause<SelectClauses>,
    FinishedState {
}
