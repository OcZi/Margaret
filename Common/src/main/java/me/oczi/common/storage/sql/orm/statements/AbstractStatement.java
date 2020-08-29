package me.oczi.common.storage.sql.orm.statements;

import me.oczi.common.api.sql.SqlTable;
import me.oczi.common.api.sql.StatementPattern;
import me.oczi.common.storage.sql.orm.statements.builder.StatementBuilder;
import me.oczi.common.storage.sql.orm.statements.builder.StatementBuilderImpl;

import java.util.Arrays;

public abstract class AbstractStatement {
  protected String retrievedBase;
  protected final StatementBuilder statementBuilder;

  protected AbstractStatement() {
    this.statementBuilder = new StatementBuilderImpl();
  }

  protected AbstractStatement(StatementPattern pattern) {
    this(pattern.getPattern());
  }

  protected AbstractStatement(String pattern) {
    this.statementBuilder = new StatementBuilderImpl(pattern);
  }

  protected AbstractStatement(StatementPattern pattern, String... columns) {
    this(pattern.getPattern(), columns);
  }

  protected AbstractStatement(String pattern, String... columns) {
    this.statementBuilder = new StatementBuilderImpl(pattern, columns);
  }

  protected AbstractStatement(StatementPattern pattern, Object... plain) {
   this(pattern.getPattern(), plain);
  }

  protected AbstractStatement(String pattern, Object... plain) {
    this.statementBuilder = new StatementBuilderImpl(
        pattern, Arrays.asList(plain));
  }

  protected AbstractStatement(StatementPattern pattern, String column, Object... param) {
    this(pattern.getPattern(), column, Arrays.asList(param));
  }

  protected AbstractStatement(String pattern, String column, Object... param) {
    this.statementBuilder = new StatementBuilderImpl(
        pattern, column, Arrays.asList(param));
  }

  protected AbstractStatement(StatementPattern pattern, SqlTable table) {
    this(pattern.getPattern(), table);
  }

  protected AbstractStatement(String pattern, SqlTable table) {
    this.statementBuilder = new StatementBuilderImpl(pattern, table);
  }

  protected StatementBuilder getStatementBuilder() {
    return statementBuilder;
  }

  @Override
  public String toString() {
    return statementBuilder.toString();
  }
}
