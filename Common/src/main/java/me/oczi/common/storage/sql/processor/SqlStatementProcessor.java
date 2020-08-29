package me.oczi.common.storage.sql.processor;

import me.oczi.common.storage.sql.orm.result.ResultMap;
import me.oczi.common.storage.sql.orm.result.SqlObject;
import me.oczi.common.storage.sql.orm.statements.data.StatementBasicData;
import me.oczi.common.storage.sql.orm.statements.data.StatementMetadata;
import me.oczi.common.storage.sql.orm.statements.prepared.PreparedStatement;

import java.util.List;
import java.util.Map;

public interface SqlStatementProcessor {

  /**
   * Execute a query to Sql DataSource
   * and get the result as Map.
   * @param statementPackage - Statement to execute.
   * @param t - Type to return.
   * @return Result cast to Map of type.
   */
  <T> Map<String, T> queryCast(PreparedStatement statementPackage,
                               Class<T> t);

  /**
   * Execute a query to Sql DataSource
   * and get the result as ResultMap.
   * @param statementPackage - Statement to execute.
   * @return Result as ResultMap.
   */
  ResultMap queryMap(PreparedStatement statementPackage);

  SqlObject queryFirst(PreparedStatement statementPackage);

  boolean queryExist(PreparedStatement statementPackage);


  /**
   * Execute a update to SQL DataSource.
   * @param statementPackage - Statement to execute
   */
  void update(PreparedStatement statementPackage);

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
