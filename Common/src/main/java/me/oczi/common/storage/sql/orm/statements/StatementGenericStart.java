package me.oczi.common.storage.sql.orm.statements;

import me.oczi.common.api.state.FinishedState;
import me.oczi.common.storage.sql.orm.dsl.clause.jump.FromJumpClause;

public interface StatementGenericStart<C>
    extends FromJumpClause<C>, FinishedState {
}
