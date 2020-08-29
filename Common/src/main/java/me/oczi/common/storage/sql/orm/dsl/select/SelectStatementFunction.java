package me.oczi.common.storage.sql.orm.dsl.select;

import me.oczi.common.api.sql.StatementPattern;

public enum SelectStatementFunction implements StatementPattern {

  COUNT("COUNT(*)");

  private final String pattern;

  SelectStatementFunction(String pattern) {
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