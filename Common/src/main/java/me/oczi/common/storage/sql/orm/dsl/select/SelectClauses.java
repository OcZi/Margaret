package me.oczi.common.storage.sql.orm.dsl.select;

import me.oczi.common.storage.sql.orm.dsl.clause.jump.LimitJumpClause;
import me.oczi.common.storage.sql.orm.dsl.clause.jump.OrderJumpClause;
import me.oczi.common.api.state.FinishedState;
import me.oczi.common.storage.sql.orm.dsl.clause.jump.WhereJumpClause;

public interface SelectClauses
    extends WhereJumpClause<SelectClauses>,
    OrderJumpClause<SelectClauses>,
    LimitJumpClause<SelectClauses>,
    FinishedState {
}
