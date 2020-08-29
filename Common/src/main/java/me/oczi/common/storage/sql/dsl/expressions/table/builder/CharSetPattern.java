package me.oczi.common.storage.sql.dsl.expressions.table.builder;

import me.oczi.common.api.sql.StatementPattern;

public enum CharSetPattern implements StatementPattern {
  DEFAULT_CHARSET("DEFAULT CHARSET = %s");

  private final String pattern;

  CharSetPattern(String pattern) {
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
