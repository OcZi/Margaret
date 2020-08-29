package me.oczi.common.storage.sql.function;

import me.oczi.common.storage.sql.orm.statements.data.SqlStatementParameterized;

public interface SqlFunction extends SqlStatementParameterized {

  String getFunction();
}
