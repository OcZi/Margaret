package me.oczi.common.storage.sql.orm.dsl.insert;

import me.oczi.common.api.sql.SqlTable;
import me.oczi.common.api.state.FinishedState;

public interface InsertIntoClause extends FinishedState {

  InsertValuesClause into(SqlTable table);
}
