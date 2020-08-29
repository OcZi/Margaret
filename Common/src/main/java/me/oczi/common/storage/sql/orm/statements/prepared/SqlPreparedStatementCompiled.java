package me.oczi.common.storage.sql.orm.statements.prepared;

import me.oczi.common.storage.sql.orm.statements.data.SqlStatementBody;
import me.oczi.common.storage.sql.orm.statements.data.SqlStatementParameterized;

public interface SqlPreparedStatementCompiled
    extends SqlStatementParameterized,
    SqlStatementBody {}
