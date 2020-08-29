package me.oczi.common.api.state;

import me.oczi.common.storage.sql.dsl.statements.prepared.PreparedStatement;

public interface FinishedState
    extends BuildableState<PreparedStatement> {
}
