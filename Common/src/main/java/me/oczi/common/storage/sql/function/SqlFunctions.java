package me.oczi.common.storage.sql.function;

import static me.oczi.common.storage.sql.function.SqlFunctionPattern.CURRENT_DATE;

public class SqlFunctions {

  public static SqlFunctionBuilder currentDate() {
    return new SqlFunctionBuilderImpl(CURRENT_DATE);
  }
}
