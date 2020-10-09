package me.oczi.common.storage.sql.dsl.statements.prepared;

import me.oczi.common.storage.sql.dsl.statements.Statement;

public interface PreparedStatement
    extends Statement {

  PreparedStatementCompiled compile();
}
