package me.oczi.common.storage.sql.orm.dsl.insert;

import me.oczi.common.api.sql.StatementPattern;
import me.oczi.common.utils.Statements;

import static me.oczi.common.storage.sql.orm.other.LogicalOperatorPattern.OR;

public enum InsertStatementPattern implements StatementPattern {

  REPLACE("REPLACE %s"),
  OR_REPLACE(OR, REPLACE),
  INTO("INTO %s"),
  MERGE("MERGE %s"),

  ON_CONFLICT_DO_UPDATE("ON CONFLICT (%s) DO UPDATE");

  private final String pattern;

  InsertStatementPattern(String expression) {
    this.pattern = expression;
  }

  InsertStatementPattern(StatementPattern... patterns) {
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
