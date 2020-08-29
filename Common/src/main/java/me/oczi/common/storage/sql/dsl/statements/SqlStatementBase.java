package me.oczi.common.storage.sql.dsl.statements;

import me.oczi.common.api.sql.StatementPattern;

public enum SqlStatementBase implements StatementPattern {

  INSERT("INSERT %s"),
  CREATE("CREATE %s"),
  SELECT("SELECT %s"),
  UPDATE("UPDATE %s"),
  DELETE("DELETE FROM %s"),

  ALTER_TABLE("ALTER TABLE %s");

  private final String pattern;

  SqlStatementBase(String pattern) {
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
