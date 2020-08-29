package me.oczi.common.storage.sql.dsl.statements.data;

import me.oczi.common.api.sql.SqlTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface StatementBasicData
    extends SqlStatementModifiable,
    StatementMetadata {

  static StatementBasicData newData(SqlTable table,
                                    List<String> columns,
                                    List<?> params) {
    return new StatementBasicDataImpl(table,
        columns,
        params);
  }

  static StatementBasicData wrapParameters(List<Object> params) {
    return new StatementBasicDataImpl(null,
        null,
        params);
  }

  static StatementBasicData wrapParameters(Object params) {
    return new StatementBasicDataImpl(null,
        Collections.singletonList(params));
  }

  static <T> List<StatementBasicData> wrapSingleParameters(List<T> params) {
    List<StatementBasicData> basicData = new ArrayList<>();
    for (Object param : params) {
      basicData.add(wrapParameters(param));
    }
    return basicData;
  }
}

