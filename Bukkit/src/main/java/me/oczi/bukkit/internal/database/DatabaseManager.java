package me.oczi.bukkit.internal.database;

import me.oczi.common.storage.sql.datasource.DataSourceType;

import java.util.List;

/**
 * Generic Database Manager interface.
 */
public interface DatabaseManager {

  /**
   * Initialize the database manager.
   */
  void init();

  /**
   * Initialize the scripts
   * of database manager.
   */
  void scriptInit();

  /**
   * Get name of database type.
   * @return Name of database type.
   */
  String getDatabaseTypeName();

  /**
   * Get {@link DataSourceType}.
   * @return datasource type.
   */
  DataSourceType getDatabaseType();

  /**
   * Get the threads used for Database manager.
   * @return threads used.
   */
  int getThreadsUsed();

  /**
   * Get the {@link DbTasks}.
   * @return Database tasks.
   */
  DbTasks getDatabaseTask();

  /**
   * Shutdown database manager.
   */
  void shutdown();

  /**
   * Get the max possible homes
   * for the database's file.
   * @return Max possible homes.
   */
  int getMaxPossibleHomes();

  /**
   * Get table of homes id.
   * @return Homes id.
   */
  List<String> getTableHomesId();
}
