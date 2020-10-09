package me.oczi.common.storage.sql.dsl.expressions.insert;

import me.oczi.common.api.sql.SqlTable;
import me.oczi.common.api.sql.FinishedState;

public interface InsertIntoClause extends FinishedState {

  InsertValuesClause into(SqlTable table);
}
