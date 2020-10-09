package me.oczi.common.api.sql;

import me.oczi.common.api.state.BuildableState;
import me.oczi.common.storage.sql.dsl.statements.prepared.PreparedStatement;

public interface FinishedState
    extends BuildableState<PreparedStatement> {
}
