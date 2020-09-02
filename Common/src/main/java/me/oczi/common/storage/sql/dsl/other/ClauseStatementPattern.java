package me.oczi.common.storage.sql.dsl.other;

import me.oczi.common.api.sql.StatementPattern;

public enum ClauseStatementPattern implements StatementPattern {

  FROM("FROM %s"),
  LIMIT("LIMIT %s"),
  OFFSET("OFFSET %s"),
  WHERE("WHERE %s"),
  ORDER("ORDER BY %s"),
  VALUES("VALUES(%s)");

  private final String pattern;

  ClauseStatementPattern(String pattern) {
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
