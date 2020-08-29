package me.oczi.common.storage.sql.orm.dsl.clause;

import me.oczi.common.api.sql.StatementPattern;

public enum LimitPattern implements StatementPattern {

  OFFSET("OFFSET");

  private final String pattern;

  LimitPattern(String pattern) {
    this.pattern = pattern;
  }

  @Override
  public String getPattern() {
    return pattern;
  }

  @Override
  public String getCleanPattern() {
    return pattern;
  }
}
