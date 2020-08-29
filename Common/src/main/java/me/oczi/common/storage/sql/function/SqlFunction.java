package me.oczi.common.storage.sql.function;

import me.oczi.common.storage.sql.dsl.statements.data.SqlStatementParameterized;

public interface SqlFunction extends SqlStatementParameterized {

  String getFunction();
}
