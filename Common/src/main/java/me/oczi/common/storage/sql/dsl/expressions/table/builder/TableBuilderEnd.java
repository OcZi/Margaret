package me.oczi.common.storage.sql.dsl.expressions.table.builder;

import me.oczi.common.api.sql.FinishedState;

public interface TableBuilderEnd
    extends FinishedState{

  FinishedState defaultCharSet(String character);
}
