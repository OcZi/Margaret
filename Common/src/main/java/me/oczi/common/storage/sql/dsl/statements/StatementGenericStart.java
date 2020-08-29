package me.oczi.common.storage.sql.dsl.statements;

import me.oczi.common.api.state.FinishedState;
import me.oczi.common.storage.sql.dsl.expressions.clause.jump.FromJumpClause;

public interface StatementGenericStart<C>
    extends FromJumpClause<C>, FinishedState {
}
