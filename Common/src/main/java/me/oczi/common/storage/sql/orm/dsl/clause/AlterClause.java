package me.oczi.common.storage.sql.orm.dsl.clause;

import me.oczi.common.storage.sql.orm.dsl.clause.recursive.alter.AddColumnsRecursive;
import me.oczi.common.storage.sql.orm.dsl.clause.recursive.alter.AddColumnsRecursiveClause;
import me.oczi.common.storage.sql.orm.statements.builder.StatementBuilder;
import me.oczi.common.utils.Statements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static me.oczi.common.storage.sql.orm.dsl.table.alter.AlterTableStatementPattern.ADD_COLUMN;
import static me.oczi.common.storage.sql.orm.dsl.table.alter.AlterTableStatementPattern.ADD_ONE_COLUMN;

public interface AlterClause {

  /**
   * Add column clause.
   * Produce "ADD COLUMN columnName dataType constraints...".
   * @param clazz Clazz to return.
   * @param builder Builder to apply clause.
   * @param columnName Column name.
   * @param dataType DataType of column.
   * @param constraints Constraints of column.
   * @param <C> Clazz
   * @return apply changes and return clazz.
   */
  static <C> C addColumn(C clazz,
                         StatementBuilder builder,
                         String columnName,
                         String dataType,
                         String... constraints) {
    builder.appendPlain(ADD_COLUMN, columnName, dataType, constraints);
    return clazz;
  }

  static <C> C addColumn(C clazz,
                         StatementBuilder builder,
                         String column) {
    builder.appendPlain(ADD_COLUMN, column);
    return clazz;
  }

  static <C> C addColumns(C clazz,
                          StatementBuilder builder,
                          String[] columns) {
    List<String> columnList = Arrays.asList(columns);
    String joinedColumns = String.join(", ", columnList);
    String placeholder = Statements
        .generateEquals(columnList.size(),
            joinedColumns,
            "(%s)",
            "%s");
    builder.appendPlain(ADD_ONE_COLUMN.getPattern(), placeholder);
    return clazz;
  }

  static <C> C addColumnsPostgres(C clazz,
                                  StatementBuilder builder,
                                  String[] columns) {
    StringBuilder stringBuilder = new StringBuilder();
    for (String column : columns) {
      if (stringBuilder.length() > 0) {
        stringBuilder.append(", ");
      }
      stringBuilder
          .append(String.format(ADD_ONE_COLUMN.getPattern(), column));
    }
    builder.appendSpace(stringBuilder.toString());
    return clazz;
  }

  static <C> C addColumnsRecursive(C clazz,
                                   StatementBuilder builder,
                                   Function<AddColumnsRecursive, AddColumnsRecursive> function) {
    StatementBuilder clauseBuilder = function
        .apply(new AddColumnsRecursiveClause(builder))
        .build();
    builder.appendStatementBuilder(clauseBuilder);
    return clazz;
  }
}
