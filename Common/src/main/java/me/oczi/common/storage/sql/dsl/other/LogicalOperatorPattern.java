package me.oczi.common.storage.sql.dsl.other;

import me.oczi.common.api.sql.StatementPattern;

public enum LogicalOperatorPattern implements StatementPattern {

  ALL("ALL %s"),
  AND("AND %s"),
  ANY("ANY %s"),
  EQUALS("= %s"),
  EXISTS("EXISTS %s"),
  BETWEEN("BETWEEN %s"),
  IF("IF %s"),
  IN("IN (%s)"),
  LIKE("LIKE %s"),
  NOT("NOT %s"),
  OR("OR %s"),
  SOME("SOME %s");

  private final String pattern;

  LogicalOperatorPattern(String pattern) {
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
