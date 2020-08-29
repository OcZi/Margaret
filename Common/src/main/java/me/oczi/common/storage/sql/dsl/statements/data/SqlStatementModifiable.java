package me.oczi.common.storage.sql.dsl.statements.data;

import me.oczi.common.api.sql.SqlTable;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface SqlStatementModifiable {

  SqlStatementModifiable setMetaData(@Nullable SqlTable table,
                                     List<String> cols,
                                     List<Object> params);

  SqlStatementModifiable setMetaData(StatementMetadata metaData);

  SqlStatementModifiable addMetaData(StatementMetadata metaData);

  SqlStatementModifiable addParameters(List<?> parameters);

  SqlStatementModifiable addParameters(Object parameter);

  SqlStatementModifiable addColumns(List<String> columns);

  SqlStatementModifiable addColumns(String column);

  SqlStatementModifiable setTable(SqlTable table);
}
