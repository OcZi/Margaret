package me.oczi.common.storage.sql.processor;

import me.oczi.common.storage.sql.dsl.result.ResultMap;
import me.oczi.common.storage.sql.dsl.result.SqlObject;
import me.oczi.common.storage.sql.dsl.statements.data.StatementBasicData;
import me.oczi.common.storage.sql.dsl.statements.prepared.PreparedStatement;

import java.util.List;
import java.util.Map;

public interface SqlStatementProcessor {

  /**
   * Execute a query to Sql DataSource
   * and get the result as Map.
   * @param statement - Statement to execute.
   * @param t - Type to return.
   * @return Result cast to Map of type.
   */
  <T> Map<String, T> queryCast(PreparedStatement statement,
                               Class<T> t);

  /**
   * Execute a query to Sql DataSource
   * and get the result as ResultMap.
   * @param statement - Statement to execute.
   * @return Result as ResultMap.
   */
  ResultMap queryMap(PreparedStatement statement);

  /**
   * Execute a Query to SQL DataSource.
   * @param statement - Statement to execute.
   * @return Get first value of object.
   */
  SqlObject queryFirstObject(PreparedStatement statement);

  /**
   * Execute a Query to SQL DataSource
   * and map the first row.
   * @param statement - Statement to execute.
   * @return Result as QueryMap.
   */
  Map<String, SqlObject> queryFirstRow(PreparedStatement statement);

  boolean queryExist(PreparedStatement statement);


  /**
   * Execute a update to SQL DataSource.
   * @param statement - Statement to execute
   */
  void update(PreparedStatement statement);

  /**
   * Execute a batch to SQL DataSource.
   * @param statements - Statement to execute
   */
  void batch(PreparedStatement... statements);

  /**
   * Execute a batch to SQL DataSource.
   * @param statements - Statement to execute
   */
  void batch(List<PreparedStatement> statements);

  /**
   * Execute a large batch to SQL DataSource.
   * @param statements - Statement to execute
   */
  void largeBatch(PreparedStatement... statements);

  /**
   * Execute a large batch to SQL DataSource.
   * @param statements - Statement to execute
   */
  void largeBatch(List<PreparedStatement> statements);


  /**
   * Execute a batch to SQL DataSource.
   * @param statementBase - Statement to execute
   */
  void reuseBatch(PreparedStatement statementBase,
                  List<StatementBasicData> batches,
                  int requiredParams);

  /**
   * Execute a batch to SQL DataSource.
   * @param statementBase - Statement to execute
   */
  void reuseLargeBatch(PreparedStatement statementBase,
                       List<StatementBasicData> batches,
                       int requiredParams);

  /**
   * Get processor.
   * @return processor
   */
  SqlProcessor getProcessor();
}
