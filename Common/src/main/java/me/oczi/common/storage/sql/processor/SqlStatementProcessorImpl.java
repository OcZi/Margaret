package me.oczi.common.storage.sql.processor;

import me.oczi.common.storage.sql.datasource.DataSource;
import me.oczi.common.storage.sql.dsl.result.ResultMap;
import me.oczi.common.storage.sql.dsl.result.SqlObject;
import me.oczi.common.storage.sql.dsl.statements.data.StatementBasicData;
import me.oczi.common.storage.sql.dsl.statements.data.StatementMetadata;
import me.oczi.common.storage.sql.dsl.statements.prepared.PreparedStatement;
import me.oczi.common.storage.sql.dsl.statements.prepared.SqlPreparedStatementCompiled;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class SqlStatementProcessorImpl implements SqlStatementProcessor {
  private final SqlProcessor delegate;

  public SqlStatementProcessorImpl(DataSource dataSource) {
    this.delegate = new SqlProcessorImpl(dataSource);
  }

  public SqlStatementProcessorImpl(boolean nullSafe, DataSource dataSource) {
    this.delegate = new SqlProcessorImpl(nullSafe, dataSource);
  }

  public SqlStatementProcessorImpl(AtomicBoolean nullSafe, DataSource dataSource) {
    this.delegate = new SqlProcessorImpl(nullSafe, dataSource);
  }

  public SqlStatementProcessorImpl(SqlProcessor processor) {
    this.delegate = processor;
  }

  @Override
  public <T> Map<String, T> queryCast(PreparedStatement preparedStatement,
                                      Class<T> type) {
    SqlPreparedStatementCompiled compiled = preparedStatement.compile();
    return delegate.queryCast(compiled.getStatement(),
        type,
        compiled.getParams());
  }

  @Override
  public ResultMap queryMap(PreparedStatement preparedStatement) {
    SqlPreparedStatementCompiled compiled = preparedStatement.compile();
    return delegate.queryMap(
        compiled.getStatement(),
        compiled.getParams());
  }

  @Override
  public SqlObject queryFirst(PreparedStatement preparedStatement) {
    SqlPreparedStatementCompiled compiled = preparedStatement.compile();
    return delegate.queryFirst(
        compiled.getStatement(),
        compiled.getParams());
  }

  @Override
  public boolean queryExist(PreparedStatement preparedStatement) {
    SqlPreparedStatementCompiled compiled = preparedStatement.compile();
    return delegate.queryExist(
        compiled.getStatement(),
        compiled.getParams());
  }

  @Override
  public void update(PreparedStatement preparedStatement) {
    SqlPreparedStatementCompiled compiled = preparedStatement.compile();
    delegate.update(
        compiled.getStatement(),
        compiled.getParams());
  }

  @Override
  public void batch(PreparedStatement... statements) {
    batch(Arrays.asList(statements));
  }

  @Override
  public void batch(List<PreparedStatement> statements) {
    delegate.batch(compileBatches(statements));
  }

  @Override
  public void largeBatch(PreparedStatement... statements) {
    largeBatch(Arrays.asList(statements));
  }

  @Override
  public void largeBatch(List<PreparedStatement> statements) {
    delegate.largeBatch(compileBatches(statements));
  }

  @Override
  public void reuseBatch(PreparedStatement statementBase,
                         List<StatementBasicData> batches,
                         int requiredParams) {
    List<Object> batchParameters =
        collectAllBatchParameters(batches);
    delegate.reusableBatch(
        statementBase.compile().getStatement(),
        batchParameters,
        batches.size(),
        requiredParams);
  }

  @Override
  public void reuseLargeBatch(PreparedStatement statementBase,
                              List<StatementBasicData> batches,
                              int requiredParams) {
    List<Object> batchParameters =
        collectAllBatchParameters(batches);
    delegate.reusableLargeBatch(
        statementBase.compile().getStatement(),
        batchParameters,
        batches.size(),
        requiredParams);
  }

  private List<Object> collectAllBatchParameters(List<StatementBasicData> batches) {
    List<Object> batchParameters = new ArrayList<>();
    for (StatementMetadata batch : batches) {
      batchParameters.addAll(batch.getParams());
    }
    return batchParameters;
  }

  private List<String> compileBatches(Collection<PreparedStatement> statements) {
    List<String> list = new ArrayList<>();
    for (PreparedStatement statement : statements) {
      String compile =
          statement
              .compile()
              .getStatement();
      list.add(compile);
    }
    return list;
  }

  @Override
  public SqlProcessor getProcessor() {
    return delegate;
  }
}
