package me.oczi.common.storage.sql.dsl.statements;

import me.oczi.common.storage.sql.dsl.statements.data.StatementBasicData;
import me.oczi.common.storage.sql.dsl.statements.data.SqlStatementBody;

public interface Statement
    extends SqlStatementBody,
    StatementBasicData {

  String getTableName();

  SqlStatementBody getBody();

  StatementBasicData getModifiableData();
}
