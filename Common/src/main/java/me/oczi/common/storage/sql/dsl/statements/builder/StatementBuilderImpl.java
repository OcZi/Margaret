package me.oczi.common.storage.sql.dsl.statements.builder;

import me.oczi.common.api.sql.SqlTable;
import me.oczi.common.api.sql.StatementPattern;
import me.oczi.common.storage.sql.dsl.other.DataType;
import me.oczi.common.storage.sql.dsl.statements.SqlStatementBodyPackage;
import me.oczi.common.storage.sql.dsl.statements.data.SqlStatementBody;
import me.oczi.common.storage.sql.dsl.statements.data.StatementMetadata;
import me.oczi.common.storage.sql.dsl.statements.data.SqlStatementModifiable;
import me.oczi.common.storage.sql.dsl.statements.prepared.PreparedStatementImpl;
import me.oczi.common.utils.CommonsUtils;
import me.oczi.common.utils.Statements;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static me.oczi.common.storage.sql.dsl.other.DataType.TABLE;

/**
 * Builder of Statement.
 */
public class StatementBuilderImpl implements StatementBuilder {
  private SqlTable table;
  private final StringBuilder patternBuilder = new StringBuilder();

  private final AtomicInteger numColumn = new AtomicInteger();
  private final List<String> cols = new ArrayList<>();
  private final List<Object> parameters = new ArrayList<>();

  public StatementBuilderImpl() {}

  public StatementBuilderImpl(String base) {
    append(base);
  }

  public StatementBuilderImpl(String base,
                              String column,
                              List<Object> param) {
    appendColumn(base, column, param);
  }

  public StatementBuilderImpl(String base,
                              String column) {
    this(base, new String[]{column});
  }

  public StatementBuilderImpl(String base,
                              String... column) {
    appendColumn(base, column);
  }

  public StatementBuilderImpl(String base,
                              List<Object> plain) {
    appendPlain(base, plain);
  }

  public StatementBuilderImpl(String base,
                              SqlTable table) {
    appendAndSetTable(base, table);
  }

  public StatementBuilderImpl(AtomicInteger numColumn) {
    setIntPlaceholders(numColumn.getAndIncrement());
  }


  public StatementBuilder addParameters(List<?> parameter) {
    if (!parameter.isEmpty()) {
      parameters.addAll(parameter);
    }
    return this;
  }

  @Override
  public StatementBuilder addParameters(Object parameter) {
    if (parameter != null) {
      parameters.add(parameter);
    }
    return this;
  }

  @Override
  public StatementBuilder addColumns(List<String> column) {
    if (!column.isEmpty()) {
      cols.addAll(column);
    }
    return this;
  }

  @Override
  public StatementBuilder addColumns(String column) {
    if (!column.isEmpty()) {
      cols.add(column);
    }
    return this;
  }

  @Override
  public StatementBuilder append(String... strings) {
    for (String string : strings) {
      if (!string.isEmpty()) {
        patternBuilder.append(string);
      }
    }
    return this;
  }

  @Override
  public StatementBuilder append(Collection<String> strings) {
    for (String string : strings) {
      if (!string.isEmpty()) {
        patternBuilder.append(string);
      }
    }
    return this;
  }

  @Override
  public StatementBuilder appendSpace(String... strings) {
    for (String s : strings) {
      if (!s.isEmpty()) {
        patternBuilder.append(" ").append(s);
      }
    }
    return this;
  }

  @Override
  public StatementBuilder appendSpace(Collection<String> strings) {
    for (String string : strings) {
      if (!string.isEmpty()) {
        patternBuilder.append(" ").append(string);
      }
    }
    return this;
  }

  @Override
  public StatementBuilder appendStatementBuilder(StatementBuilder otherBuilder) {
    appendSpace(otherBuilder.getPatternBuilder().toString())
        .addMetaData(otherBuilder.getModifiableData());
    addIntPlaceholders(otherBuilder.getNumColumn().get());
    return this;
  }

  @Override
  public StatementBuilder appendColumn(StatementPattern pattern,
                                       String column) {
    return appendColumn(pattern.getPattern(),
        column);
  }

  @Override
  public StatementBuilder appendColumn(List<StatementPattern> patterns,
                                       String column) {
    return appendColumn(
        Statements.formatPatterns(patterns),
        column);
  }

  @Override
  public StatementBuilder appendColumn(StatementPattern pattern,
                                       String... column) {
    return appendColumn(pattern.getPattern(), column);
  }

  @Override
  public StatementBuilder appendColumn(String pattern,
                                       String... column) {
    return appendColumn(pattern,
        CommonsUtils.arrayToString(column));
  }

  @Override
  public StatementBuilder appendColumn(String pattern,
                                       List<?> columns) {
    return appendColumn(pattern,
        CommonsUtils.joinIterable(columns));
  }

  @Override
  public StatementBuilder appendColumn(StatementPattern pattern,
                                       String column,
                                       List<?> parameters) {
    return appendColumn(pattern.getPattern(),
        column,
        parameters);
  }

  @Override
  public StatementBuilder appendColumn(String pattern,
                                       String column) {
    return appendColumn(pattern,
        column,
        Collections.emptyList());
  }

  @Override
  public StatementBuilder appendColumn(List<StatementPattern> patterns,
                                       String column,
                                       List<?> parameters) {
    return appendColumn(
        Statements.formatPatterns(patterns),
        column,
        parameters);
  }

  @Override
  public StatementBuilder appendColumn(String pattern,
                                       String column,
                                       List<?> param) {
    String columnPlaceholder = DataType.COLUMN
        .getPlaceholder(numColumn.getAndIncrement());
    String paramClause = !param.isEmpty()
        ? Statements.generateEquals(param.size())
        : "";

    appendFormatBase(pattern, columnPlaceholder)
        .appendSpace(paramClause)
        .addColumns(column)
        .addParameters(param);
    return this;
  }

  @Override
  public StatementBuilder appendPlain(StatementPattern pattern,
                                      Object plainText) {
    return appendPlain(pattern.getPattern(), plainText);
  }

  @Override
  public StatementBuilder appendPlain(StatementPattern pattern,
                                      List<?> plainText) {
    return appendPlain(pattern.getPattern(), plainText);
  }

  @Override
  public StatementBuilder appendPlain(String pattern,
                                      List<?> plainText) {
    String format = String.format(pattern,
        CommonsUtils.joinIterable(plainText));
    return patternBuilder.length() > 1
        ? appendSpace(format) : append(format);
  }

  @Override
  public StatementBuilder appendPlain(StatementPattern pattern,
                                      Object... plainText) {
    return appendPlain(pattern.getPattern(), plainText);
  }

  @Override
  public StatementBuilder appendPlain(String string,
                                      Object... plainText) {
    String format = plainText.length >= 1
        ? String.format(string, plainText)
        : string;
    return patternBuilder.length() > 1
        ? appendSpace(format) : append(format);
  }

  @Override
  public StatementBuilder appendAndSetTable(List<StatementPattern> patterns,
                                            SqlTable table) {
    return appendAndSetTable(
        Statements.formatPatterns(patterns),
        table);
  }

  @Override
  public StatementBuilder appendAndSetTable(StatementPattern pattern,
                                            SqlTable table) {
    return appendAndSetTable(pattern.getPattern(), table);
  }

  @Override
  public StatementBuilder appendAndSetTable(String pattern,
                                            SqlTable table) {
    setTable(table);
    String format = String.format(pattern,
        TABLE.getPlaceholder());
    return patternBuilder.length() > 1
        ? appendSpace(format) : append(format);
  }

  @Override
  public StatementBuilder appendFormatBase(String base,
                                           String... formatList) {
    return appendFormatBase(base, Arrays.asList(formatList));
  }

  @Override
  public StatementBuilder appendFormatBase(String base,
                                           List<String> formatList) {
    base = String.format(base,
        CommonsUtils.cyclicFormat(formatList, true));
    return patternBuilder.length() > 1
        ? appendSpace(base) : append(base);
  }

  @Override
  public StatementBuilder appendFormatBase(String base, String format) {
    base = String.format(base, format);
    return patternBuilder.length() > 1
        ? appendSpace(base) : append(base);
  }

  @Override
  public StatementBuilder formatLastClause(Object... objects) {
    String build = patternBuilder.toString();
    clearStatement();
    patternBuilder.append(String.format(build, objects));
    return this;
  }

  @Override
  public StatementBuilder clearStatement() {
    patternBuilder.delete(0, patternBuilder.length());
    return this;
  }

  @Override
  public StatementBuilder clearAnything() {
    clearStatement();
    this.numColumn.set(0);
    this.parameters.clear();
    this.cols.clear();
    this.table = null;
    return this;
  }

  @Override
  public StatementBuilder setMetaData(@Nullable SqlTable table,
                                      List<String> cols,
                                      List<Object> params) {
    this.cols.clear();
    this.parameters.clear();
    addParameters(params)
        .addColumns(cols);

    if (table != null) {
      setTable(table);
    }
    return this;
  }

  @Override
  public SqlStatementModifiable setMetaData(StatementMetadata metaData) {
    return setMetaData(
        metaData.getTable(),
        metaData.getColumns(),
        metaData.getParams());
  }

  @Override
  public SqlStatementModifiable addMetaData(StatementMetadata metaData) {
    return addColumns(metaData.getColumns())
        .addParameters(metaData.getParams());
  }

  @Override
  public StatementBuilder setTable(SqlTable table) {
    this.table = table;
    return this;
  }

  @Override
  public StatementBuilder setIntPlaceholders(int numColumn) {
    this.numColumn.set(numColumn);
    return this;
  }

  @Override
  public StatementBuilderBasicData getModifiableData() {
    return this;
  }

  @Override
  public StatementBuilder addIntPlaceholders(int numColumn) {
    this.numColumn.addAndGet(numColumn);
    return this;
  }

  public PreparedStatementImpl build() {
    return new PreparedStatementImpl(patternBuilder.toString(),
        numColumn.get(),
        parameters.size(),
        this);
  }

  @Override
  public SqlTable getTable() {
    return table;
  }

  @Override
  public StringBuilder getPatternBuilder() {
    return patternBuilder;
  }

  @Override
  public AtomicInteger getNumColumn() {
    return numColumn;
  }

  @Override
  public int getNumParameter() {
    return parameters.size();
  }

  @Override
  public String getStatement() {
    return patternBuilder.toString();
  }

  @Override
  public String getTableName() {
    return table != null
        ? table.getName()
        : "null";
  }

  @Override
  public List<Object> getParams() {
    return parameters;
  }

  @Override
  public List<String> getColumns() {
    return cols;
  }

  @Override
  public SqlStatementBody getBody() {
    return new SqlStatementBodyPackage(patternBuilder.toString());
  }

  @Override
  public String toString() {
    return "StatementBuilder{" +
        "table=" + getTableName() +
        ", patternBuilder=" + patternBuilder +
        ", numColumn=" + numColumn +
        ", columns=" + cols +
        ", parameters=" + parameters +
        '}';
  }
}
