package me.oczi.common.storage.sql.dsl.expressions.table.builder;

import me.oczi.common.api.sql.SqlTable;
import me.oczi.common.api.sql.FinishedState;
import me.oczi.common.storage.sql.datasource.DataSourceType;
import me.oczi.common.storage.sql.dsl.expressions.clause.ValuesClause;
import me.oczi.common.storage.sql.dsl.statements.AbstractStatement;
import me.oczi.common.storage.sql.dsl.statements.prepared.PreparedStatement;
import me.oczi.common.utils.CommonsUtils;
import org.jetbrains.annotations.NotNull;

import static me.oczi.common.storage.sql.dsl.expressions.table.builder.CreateStatementPattern.*;

public class TableBuilderImpl
    extends AbstractStatement
    implements TableBuilderStart{
  private boolean hasClause;
  private String charSet;
  private final SqlTable table;
  private final DataSourceType dataSourceType;

  public TableBuilderImpl(@NotNull SqlTable table,
                          DataSourceType dataSourceType) {
    super(CREATE_TABLE);
    this.dataSourceType = dataSourceType;
    this.table = table;
    statementBuilder.setTable(table);
  }

  @Override
  public TableBuilderEnd ifNotExist() {
    String tableFormat = String.format(
        IF_NOT_EXISTS.getPattern(), table.getName());
    statementBuilder.formatLastClause(tableFormat);
    this.hasClause = true;
    return this;
  }

  @Override
  public PreparedStatement build() {
    if (!hasClause) {
      statementBuilder.formatLastClause(table.getName());
    }
    ValuesClause.createValues(statementBuilder, dataSourceType);
    if (!CommonsUtils.isNullOrEmpty(charSet) &&
        dataSourceType.equals(DataSourceType.MYSQL)) {
      statementBuilder.appendPlain(
          CharSetPattern.DEFAULT_CHARSET, charSet);
    }
    return statementBuilder.build();
  }

  @Override
  public FinishedState defaultCharSet(String character) {
    this.charSet = character;
    return this;
  }
}
