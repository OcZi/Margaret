package me.oczi.common.storage.sql.dsl.expressions.update;

import me.oczi.common.api.sql.StatementPattern;

public enum UpdateStatementPattern implements StatementPattern {

  SET("SET %s");

  private final String pattern;

  UpdateStatementPattern(String pattern) {
    this.pattern = pattern;
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
