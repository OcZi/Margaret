package me.oczi.common.storage.sql.orm.dsl.table.builder;

import me.oczi.common.api.state.FinishedState;

public interface TableBuilderEnd
    extends FinishedState{

  FinishedState defaultCharSet(String character);
}
