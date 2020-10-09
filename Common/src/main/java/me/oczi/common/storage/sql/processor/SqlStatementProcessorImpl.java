package me.oczi.common.storage.sql.processor;

import me.oczi.common.storage.sql.datasource.DataSource;
import me.oczi.common.storage.sql.dsl.result.ResultMap;
import me.oczi.common.storage.sql.dsl.result.SqlObject;
import me.oczi.common.storage.sql.dsl.statements.data.StatementBasicData;
import me.oczi.common.storage.sql.dsl.statements.data.StatementMetadata;
import me.oczi.common.storage.sql.dsl.statements.prepared.PreparedStatement;
import me.oczi.common.storage.sql.dsl.statements.prepared.PreparedStatementCompiled;

import java.util.*;

public class SqlStatementProcessorImpl implements SqlStatementProcessor {
  private final SqlProcessor delegate;

  public SqlStatementProcessorImpl(DataSource dataSource) {
    this(false, dataSource);
  }

  public SqlStatementProcessorImpl(boolean nullSafe, DataSource dataSource) {
    this.delegate = new SqlProcessorImpl(nullSafe, dataSource);
  }

  public SqlStatementProcessorImpl(SqlProcessor processor) {
    this.delegate = processor;
  }

  @Override
  public <T> Map<String, T> queryCast(PreparedStatement statement,
                                      Class<T> type) {
    PreparedStatementCompiled compiled = statement.compile();
    return delegate.queryCast(compiled.getStatement(),
        type,
        compiled.getParams());
  }

  @Override
  public ResultMap queryMap(PreparedStatement statement) {
    PreparedStatementCompiled compiled = statement.compile();
    return delegate.queryMap(
        compiled.getStatement(),
        compiled.getParams());
  }

  @Override
  public SqlObject queryFirstObject(PreparedStatement statement) {
    PreparedStatementCompiled compiled = statement.compile();
    return delegate.queryFirstObject(
        compiled.getStatement(),
        compiled.getParams());
  }

  @Override
  public Map<String, SqlObject> queryFirstRow(PreparedStatement statement) {
    PreparedStatementCompiled compiled = statement.compile();
    return delegate.queryFirstRow(
        compiled.getStatement(),
        compiled.getParams());
  }

  @Override
  public boolean queryExist(PreparedStatement statement) {
    PreparedStatementCompiled compiled = statement.compile();
    return delegate.queryExist(
        compiled.getStatement(),
        compiled.getParams());
  }

  @Override
  public void update(PreparedStatement statement) {
    PreparedStatementCompiled compiled = statement.compile();
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
