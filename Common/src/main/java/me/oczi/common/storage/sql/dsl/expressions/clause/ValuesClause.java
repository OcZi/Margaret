package me.oczi.common.storage.sql.dsl.expressions.clause;

import me.oczi.common.api.sql.SqlTable;
import me.oczi.common.storage.sql.dsl.statements.builder.StatementBuilder;
import me.oczi.common.utils.CommonsUtils;
import me.oczi.common.utils.Statements;
import me.oczi.common.storage.sql.datasource.DataSourceType;

import java.util.Arrays;
import java.util.List;

import static me.oczi.common.storage.sql.dsl.expressions.insert.InsertStatementPattern.ON_CONFLICT_DO_UPDATE;
import static me.oczi.common.storage.sql.dsl.expressions.update.UpdateStatementPattern.SET;
import static me.oczi.common.storage.sql.dsl.other.ClauseStatementPattern.VALUES;

public interface ValuesClause {

  static void createValues(StatementBuilder builder,
                           DataSourceType dataSourceType) {
    String columnJoined = String.join(", ",
        builder.getTable().getColumns());

    columnJoined = CommonsUtils.middleSplit("()", columnJoined);
    builder.appendSpace(Statements
        .formatConstraintCompatibility(columnJoined, dataSourceType));
  }

  static void insertValues(StatementBuilder builder,
                           Object... values) {
    insertValues(builder, Arrays.asList(values));
  }

  static void insertValues(StatementBuilder builder,
                           List<?> values) {
    SqlTable table = builder.getTable();
    if (table == null) {
      throw new NullPointerException("Statement's Table is null.");
    }
    if (table.getColumns().length > values.size()) {
      throw new IndexOutOfBoundsException(
          "Inserted values not satisfy all the columns of table " + table.getName()
              + ". Columns needed: " + table.getColumns().length
              + ", Values inserted: " + values.size());
    }

    String placeholder = CommonsUtils.joinRepeatedString(
        "?", ", ", values.size());
    placeholder = String.format(VALUES.getPattern(), placeholder);
    builder.appendSpace(placeholder)
        .addParameters(values);
  }

  static void onConflictDoUpdate(StatementBuilder builder,
                                 Object... values) {
    onConflictDoUpdate(builder, Arrays.asList(values));
  }

  /**
   * On conflict clause.
   * ONLY UPDATE SET FOR COLUMNS IN ORDER OF THE TABLE.
   * IF THE ORIGINAL DML NOT USE THEM COLUMNS
   * PREPARE FOR A EXCEPTION.
   * NOT USE IN PRODUCTION EXCEPT YOU REALLY KNOW THIS LIMITATION.
   * @param builder StatementBuilder to apply.
   * @param values Values to insert.
   */
  static void onConflictDoUpdate(StatementBuilder builder,
                                 List<?> values) {
    SqlTable table = builder.getTable();
    if (table == null) {
      throw new NullPointerException("Statement's Table is null.");
    }
    StringBuilder clauseBuilder = new StringBuilder();
    String prefix = "excluded.";
    String id = Statements
        .getColumnName(table.getColumns()[0]);
    builder.appendPlain(ON_CONFLICT_DO_UPDATE, id);
    for (int i = 0; i < values.size(); i++) {
      if (clauseBuilder.length() > 0) {
        clauseBuilder.append(", ");
      }
      String columnName = Statements
          .getColumnName(table.getColumns()[i]);
      clauseBuilder.append(columnName)
          .append(" = ")
          .append(prefix)
          .append(columnName);
    }
    builder.appendPlain(SET, clauseBuilder.toString());
  }
}
