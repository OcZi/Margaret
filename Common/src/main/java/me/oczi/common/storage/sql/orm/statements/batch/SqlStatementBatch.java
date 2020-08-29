package me.oczi.common.storage.sql.orm.statements.batch;

import me.oczi.common.storage.sql.orm.statements.prepared.PreparedStatement;

// Unused
public interface SqlStatementBatch {

  SqlStatementBatch addBatch(PreparedStatement mutable);


}
