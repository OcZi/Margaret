package me.oczi.common.storage.sql.dsl.expressions.clause.recursive.alter;

import me.oczi.common.api.state.UnfinishedState;

public interface AddColumnsRecursive extends UnfinishedState {

  AddColumnsRecursive add(String columnName,
                          String dataType,
                          String... constraints);

  AddColumnsRecursive add(String column);


  default AddColumnsRecursive addColumns(String columnName,
                                         String dataType,
                                         String... constraints) {
    return add(columnName, dataType, constraints);
  }

  default AddColumnsRecursive addColumns(String column) {
    return add(column);
  }
}
