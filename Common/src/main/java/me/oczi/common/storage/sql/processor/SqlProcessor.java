package me.oczi.common.storage.sql.processor;

import me.oczi.common.storage.sql.dsl.result.ResultMap;
import me.oczi.common.storage.sql.dsl.result.SqlObject;

import java.util.List;
import java.util.Map;

/**
 * Processor of SQL statements.
 */
public interface SqlProcessor {

  /**
   * Execute a Query to SQL DataSource.
   * @param expression - Expression to execute
   * @param params - Parameters to apply to PreparedStatement.
   * @return Get first value of object.
   */
  SqlObject queryFirstObject(String expression,
                             List<Object> params);

  /**
   * Execute a Query to SQL DataSource.
   * @param expression - Expression to execute
   * @param params - Parameters to apply to PreparedStatement.
   * @return Get first value of object.
   */
  SqlObject queryFirstObject(String expression,
                             Object... params);

  /**
   * Execute a Query to SQL DataSource
   * and map the first row.
   * @param expression - Expression to execute
   * @param params - Parameters to apply to PreparedStatement.
   * @return Result as QueryMap.
   */
  Map<String, SqlObject> queryFirstRow(String expression,
                          List<Object> params);

  /**
   * Execute a Query to SQL DataSource
   * and map the first row.
   * @param expression - Expression to execute
   * @param params - Parameters to apply to PreparedStatement.
   * @return Result as QueryMap.
   */
  Map<String, SqlObject> queryFirstRow(String expression,
                          Object... params);

  /**
   * Execute a Query to SQL DataSource
   * and map all the rows.
   * @param expression - Expression to execute
   * @param params - Parameters to apply to PreparedStatement.
   * @return Result as QueryMap.
   */
  ResultMap queryMap(String expression,
                     Object... params);

  /**
   * Execute a Query to SQL DataSource
   * and map the result.
   * @param expression - Expression to execute
   * @param params - Parameters to apply to PreparedStatement.
   * @return Result as QueryMap.
   */
  ResultMap queryMap(String expression,
                     List<Object> params);

  boolean queryExist(String expression,
                     Object... params);

  boolean queryExist(String expression,
                     List<Object> params);

  /**
   * Execute a Query to SQL DataSource
   * and cast the map result.
   * @param expression - Expression to execute
   * @param clazz - Type to cast
   * @param params - Parameters to apply to PreparedStatement.
   * @return Result cast to Map and type.
   */
  <T> Map<String, T> queryCast(String expression,
                               Class<T> clazz,
                               Object... params);

  /**
   * Execute a Query to SQL DataSource
   * and cast the map result.
   * @param expression - Expression to execute
   * @param clazz - Type to cast
   * @param params - Parameters to apply to PreparedStatement.
   * @return Result cast to Map and type.
   */
  <T> Map<String, T> queryCast(String expression,
                          Class<T> clazz,
                          List<Object> params);

  /**
   * Execute a update to SQL DataSource.
   * @param expression - Expression to execute
   */
  void update(String expression, List<Object> params);

  /**
   * Execute a update to SQL DataSource.
   * @param expression - Expression to execute
   */
  void update(String expression, Object... params);

  /**
   * Execute a batch to SQL DataSource.
   * @param expression - Expression to execute
   */
  void batch(String... expression);

  /**
   * Execute a batch to SQL DataSource.
   * @param expression - Expression to execute
   */
  void batch(List<String> expression);

  /**
   * Execute a large batch to SQL DataSource.
   * @param expression - Expression to execute
   */
  void largeBatch(String... expression);

  /**
   * Execute a large batch to SQL DataSource.
   * @param expression - Expression to execute
   */
  void largeBatch(List<String> expression);

  /**
   * Reuse a expression to create
   * multiples batches and execute them.
   * @param expression Expression to execute
   * @param totalParameters All the parameters of batches.
   * @param batches Batches to create.
   * @param paramSize parameter's size of each batch.
   */
  void reusableBatch(String expression,
                     List<Object> totalParameters,
                     int batches,
                     int paramSize);

  /**
   * Reuse a expression to create
   * multiples larges batches and execute them.
   * @param expression Expression to execute
   * @param totalParameters All the parameters of batches.
   * @param batches Batches to create.
   * @param paramSize parameter's size of each batch.
   */
  void reusableLargeBatch(String expression,
                          List<Object> totalParameters,
                          int batches,
                          int paramSize);
}
