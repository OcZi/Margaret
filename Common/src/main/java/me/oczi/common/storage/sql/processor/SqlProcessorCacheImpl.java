package me.oczi.common.storage.sql.processor;

import me.oczi.common.storage.sql.datasource.DataSource;
import me.oczi.common.storage.sql.dsl.expressions.SqlDsl;
import me.oczi.common.storage.sql.dsl.result.ResultMap;
import me.oczi.common.storage.sql.dsl.result.SqlObject;
import me.oczi.common.storage.sql.dsl.statements.data.StatementMetadata;
import me.oczi.common.storage.sql.dsl.statements.prepared.PreparedStatement;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class SqlProcessorCacheImpl implements SqlProcessorCache {
  private final Map<String, PreparedStatement> cache;

  private final SqlDsl dsl;
  private final SqlStatementProcessor statementProcessor;

  public SqlProcessorCacheImpl(Map<String, PreparedStatement> cache,
                               SqlDsl dsl,
                               DataSource dataSource) {
    this(cache, dsl, false, dataSource);
  }

  public SqlProcessorCacheImpl(Map<String, PreparedStatement> cache,
                               SqlDsl dsl,
                               boolean nullSafe,
                               DataSource dataSource) {
    this.cache = cache;
    this.dsl = dsl;
    this.statementProcessor = new SqlStatementProcessorImpl(nullSafe, dataSource);
  }

  public SqlProcessorCacheImpl(Map<String, PreparedStatement> cache,
                               SqlDsl dsl,
                               SqlStatementProcessor statementProcessor) {
    this.cache = cache;
    this.dsl = dsl;
    this.statementProcessor = statementProcessor;
  }

  private PreparedStatement process(String idStatement,
                                    Function<SqlDsl, PreparedStatement> function) {
    // No process any statement with empty id.
    return idStatement.isEmpty()
        ? function.apply(dsl)
        : cache.computeIfAbsent(idStatement,
          (k) -> function.apply(dsl));
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
  public SqlObject queryFirstObject(String idStatement,
                                    StatementMetadata metaData,
                                    Function<SqlDsl, PreparedStatement> function) {
    return statementProcessor.queryFirstObject(
        processStatement(idStatement, metaData, function));
  }

  @Override
  public Map<String, SqlObject> queryFirstRow(String idStatement,
                                              StatementMetadata metaData,
                                              Function<SqlDsl, PreparedStatement> function) {
    return statementProcessor.queryFirstRow(
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
  public SqlStatementProcessor getStatementProcessor() {
    return statementProcessor;
  }

  @Override
  public Set<String> getStatementsCached() {
    return cache.keySet();
  }

  @Override
  public SqlDsl getDslStatements() {
    return dsl;
  }
}
