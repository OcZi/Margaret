package me.oczi.common.storage.sql.orm.statements;

import me.oczi.common.storage.sql.orm.statements.data.StatementBasicData;
import me.oczi.common.storage.sql.orm.statements.data.SqlStatementBody;

public interface Statement
    extends SqlStatementBody,
    StatementBasicData {

  String getTableName();

  SqlStatementBody getBody();

  StatementBasicData getModifiableData();
}
