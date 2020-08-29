package me.oczi.common.storage.sql.dsl.expressions.table.builder;

import me.oczi.common.api.sql.StatementPattern;
import me.oczi.common.utils.Statements;

import static me.oczi.common.storage.sql.dsl.other.LogicalOperatorPattern.*;

public enum CreateStatementPattern implements StatementPattern {

  CREATE_TABLE("CREATE TABLE %s"),

  MERGE_INTO("MERGE INTO %s"),
  IF_NOT_EXISTS(IF, NOT, EXISTS);

  private final String pattern;

  CreateStatementPattern(String pattern) {
    this.pattern = pattern;
  }

  CreateStatementPattern(StatementPattern... patterns) {
    this.pattern = Statements.formatPatterns(patterns);
  }

  @Override
  public String getPattern() {
    return pattern;
  }

  @Override
  public String getCleanPattern() {
    return String.format(pattern, "").trim();
  }
}
