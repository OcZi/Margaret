package me.oczi.common.storage.sql.dsl.statements.data;

import java.util.List;

public interface SqlStatementParameterized {

  List<Object> getParams();
}
