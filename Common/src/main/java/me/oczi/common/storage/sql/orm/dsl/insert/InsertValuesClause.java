package me.oczi.common.storage.sql.orm.dsl.insert;

import me.oczi.common.api.state.FinishedState;
import me.oczi.common.storage.sql.orm.dsl.clause.jump.ValuesJumpClause;

public interface InsertValuesClause
    extends ValuesJumpClause, FinishedState {
}
