package me.oczi.common.storage.sql.dsl.expressions.table.builder;

import me.oczi.common.api.state.FinishedState;

public interface TableBuilderEnd
    extends FinishedState{

  FinishedState defaultCharSet(String character);
}
