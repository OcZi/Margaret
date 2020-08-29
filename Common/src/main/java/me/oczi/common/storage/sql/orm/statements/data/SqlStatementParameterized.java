package me.oczi.common.storage.sql.orm.statements.data;

import java.util.List;

public interface SqlStatementParameterized {

  List<Object> getParams();
}
