package me.oczi.common.storage.sql.dsl.expressions.insert;

import me.oczi.common.api.sql.FinishedState;
import me.oczi.common.storage.sql.dsl.expressions.clause.jump.ValuesJumpClause;

public interface InsertValuesClause
    extends ValuesJumpClause, FinishedState {
}
