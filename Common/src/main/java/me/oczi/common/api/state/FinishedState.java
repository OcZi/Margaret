package me.oczi.common.api.state;

import me.oczi.common.storage.sql.orm.statements.prepared.PreparedStatement;

public interface FinishedState
    extends BuildableState<PreparedStatement> {
}
