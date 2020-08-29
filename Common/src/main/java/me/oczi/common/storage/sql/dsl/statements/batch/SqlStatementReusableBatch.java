package me.oczi.common.storage.sql.dsl.statements.batch;

import me.oczi.common.storage.sql.dsl.statements.data.SqlStatementBody;
import me.oczi.common.storage.sql.dsl.statements.data.SqlStatementParameterized;

public interface SqlStatementReusableBatch
    extends SqlStatementBody,
    SqlStatementParameterized {

  SqlStatementReusableBatch addStatement(SqlStatementParameterized parameterized);

  int getRequiredParameters();
}
