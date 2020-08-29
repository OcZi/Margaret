package me.oczi.common.storage.sql.dsl.statements.prepared;

import me.oczi.common.storage.sql.dsl.statements.data.SqlStatementBody;
import me.oczi.common.storage.sql.dsl.statements.data.SqlStatementParameterized;

public interface SqlPreparedStatementCompiled
    extends SqlStatementParameterized,
    SqlStatementBody {}
