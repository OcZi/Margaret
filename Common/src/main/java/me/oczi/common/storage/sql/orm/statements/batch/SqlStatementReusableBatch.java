package me.oczi.common.storage.sql.orm.statements.batch;

import me.oczi.common.storage.sql.orm.statements.data.SqlStatementBody;
import me.oczi.common.storage.sql.orm.statements.data.SqlStatementParameterized;

public interface SqlStatementReusableBatch
    extends SqlStatementBody,
    SqlStatementParameterized {

  SqlStatementReusableBatch addStatement(SqlStatementParameterized parameterized);

  int getRequiredParameters();
}
