package me.oczi.common.api.sql;

/**
 * Patterns of SQL Statements.
 */
public interface StatementPattern {

  /**
   * Get the pattern schematic.
   * @return Pattern
   */
  String getPattern();

  /**
   * Get the pattern without schematic.
   * @return Pattern
   */
  String getCleanPattern();
}
