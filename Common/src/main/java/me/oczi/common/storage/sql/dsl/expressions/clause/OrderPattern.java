package me.oczi.common.storage.sql.dsl.expressions.clause;

import me.oczi.common.api.sql.StatementPattern;

public enum OrderPattern implements StatementPattern {

  ASCENDING("ASC"),
  DESCENDING("DESC");

  private final String pattern;

  OrderPattern(String pattern) {
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
