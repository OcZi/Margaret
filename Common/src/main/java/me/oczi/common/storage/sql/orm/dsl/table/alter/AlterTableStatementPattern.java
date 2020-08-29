package me.oczi.common.storage.sql.orm.dsl.table.alter;

import me.oczi.common.api.sql.StatementPattern;

public enum AlterTableStatementPattern implements StatementPattern {
  ADD_COLUMN("ADD %s %s %s"),

  ADD_ONE_COLUMN("ADD %s");

  private final String pattern;

  AlterTableStatementPattern(String pattern) {
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
