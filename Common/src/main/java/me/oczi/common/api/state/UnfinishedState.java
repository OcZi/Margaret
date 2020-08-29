package me.oczi.common.api.state;

import me.oczi.common.storage.sql.orm.statements.builder.StatementBuilder;

public interface UnfinishedState
    extends BuildableState<StatementBuilder> {
}
