package me.oczi.common.storage.sql.orm.statements.prepared;

import me.oczi.common.storage.sql.orm.statements.Statement;

public interface PreparedStatement
    extends Statement {

  SqlPreparedStatementCompiled compile();
}
