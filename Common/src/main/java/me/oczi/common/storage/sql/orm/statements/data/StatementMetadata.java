package me.oczi.common.storage.sql.orm.statements.data;

import me.oczi.common.api.sql.SqlTable;

import java.util.List;

public interface StatementMetadata extends SqlStatementParameterized {

  SqlStatementModifiable getModifiableData();

  List<String> getColumns();

  SqlTable getTable();
}
