package me.oczi.common.storage.sql.orm.dsl.table.alter;

import me.oczi.common.storage.sql.orm.dsl.clause.recursive.alter.AddColumnsRecursive;

import java.util.function.Function;

public interface AlterTableStart {

  /*
  AlterTableClauses addColumns(String columnName,
                               String dataType,
                               String... constraints);
   */

  AlterTableClauses addColumn(String column);

  AlterTableClauses addColumns(String[] column);

  AlterTableClauses addColumnRecursive(Function<AddColumnsRecursive, AddColumnsRecursive> function);
}
