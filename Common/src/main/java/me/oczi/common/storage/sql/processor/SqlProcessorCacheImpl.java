package me.oczi.common.storage.sql.processor;

import me.oczi.common.storage.sql.datasource.DataSource;
import me.oczi.common.storage.sql.dsl.result.ResultMap;
import me.oczi.common.storage.sql.dsl.result.SqlObject;
import me.oczi.common.storage.sql.dsl.expressions.SqlDsl;
import me.oczi.common.storage.sql.dsl.statements.data.StatementMetadata;
import me.oczi.common.storage.sql.dsl.statements.prepared.PreparedStatement;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

public class SqlProcessorCacheImpl implements SqlProcessorCache {
  private final Map<String, PreparedStatement> cache;

  private final SqlDsl dao;
  private final SqlStatementProcessor statementProcessor;

  public SqlProcessorCacheImpl(Map<String, PreparedStatement> cache,
                               SqlDsl dao,
                               DataSource dataSource) {
    this(cache, dao, false, dataSource);
  }

  public SqlProcessorCacheImpl(Map<String, PreparedStatement> cache,
                               SqlDsl dao,
                               boolean nullSafe,
                               DataSource dataSource) {
    this(cache, dao, new AtomicBoolean(nullSafe), dataSource);
  }

  public SqlProcessorCacheImpl(Map<String, PreparedStatement> cache,
                               SqlDsl dao,
                               AtomicBoolean nullSafe,
                               DataSource dataSource) {
    this(cache, dao, new SqlStatementProcessorImpl(nullSafe, dataSource));
  }

  public SqlProcessorCacheImpl(Map<String, PreparedStatement> cache,
                               SqlDsl dao,
                               SqlStatementProcessor statementProcessor) {
    this.cache = cache;
    this.dao = dao;
    this.statementProcessor = statementProcessor;
  }

  private PreparedStatement process(String idStatement,
                                    Function<SqlDsl, PreparedStatement> function) {
    PreparedStatement statement;
    if (!cache.containsKey(idStatement)) {
      statement = function.apply(dao);
      cache.put(idStatement, statement);
    } else {
      statement = cache.get(idStatement);
    }
    return statement;
  }

  private PreparedStatement processStatement(String idStatement,
                                             StatementMetadata metaData,
                                             Function<SqlDsl, PreparedStatement> function) {
    PreparedStatement statement = process(idStatement, function);
    statement.setMetaData(metaData);
    return statement;
  }

  @Override
  public ResultMap queryMap(String idStatement,
                            StatementMetadata metaData,
                            Function<SqlDsl, PreparedStatement> function) {
    return statementProcessor.queryMap(
        processStatement(idStatement, metaData, function));
  }

  @Override
  public <T> Map<String, T> queryCast(String idStatement,
                                      StatementMetadata metaData,
                                      Class<T> type,
                                      Function<SqlDsl, PreparedStatement> function) {
    return statementProcessor.queryCast(
        processStatement(idStatement, metaData, function), type);
  }

  @Override
  public SqlObject queryFirst(String idStatement,
                              StatementMetadata metaData,
                              Function<SqlDsl, PreparedStatement> function) {
    return statementProcessor.queryFirst(
        processStatement(idStatement, metaData, function));
  }

  @Override
  public boolean queryExist(String idStatement,
                            StatementMetadata metaData,
                            Function<SqlDsl, PreparedStatement> function) {
    return statementProcessor.queryExist(
        processStatement(idStatement, metaData, function));
  }

  @Override
  public void update(String idStatement,
                     StatementMetadata metaData,
                     Function<SqlDsl, PreparedStatement> function) {
    statementProcessor.update(
        processStatement(idStatement, metaData, function));
  }

  @Override
  public void batch(String idStatement,
                    Function<SqlDsl, PreparedStatement> function) {
    statementProcessor.batch(
        process(idStatement, function));
  }

  @Override
  public void batch(String idStatement,
                    StatementMetadata metaData,
                    Function<SqlDsl, PreparedStatement> function) {
    statementProcessor.batch(
        processStatement(idStatement, metaData, function));
  }

  @Override
  public SqlStatementProcessor getProcessorAdapter() {
    return statementProcessor;
  }

  @Override
  public Set<String> getStatementsCached() {
    return cache.keySet();
  }

  @Override
  public SqlDsl getDaoStatements() {
    return dao;
  }
}
