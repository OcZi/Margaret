package me.oczi.common.executors;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import me.oczi.common.storage.sql.dsl.expressions.SqlDsl;
import me.oczi.common.storage.sql.dsl.result.ResultMap;
import me.oczi.common.storage.sql.dsl.result.SqlObject;
import me.oczi.common.storage.sql.dsl.statements.data.StatementMetadata;
import me.oczi.common.storage.sql.dsl.statements.prepared.PreparedStatement;
import me.oczi.common.storage.sql.processor.SqlProcessor;
import me.oczi.common.storage.sql.processor.SqlProcessorCache;
import me.oczi.common.storage.sql.processor.SqlStatementProcessor;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

/**
 * A {@link TaskExecutor} with {@link SqlProcessor} Integrated.
 */
public class SqlTaskExecutor extends TaskExecutor implements SqlProcessorCache {
  private final SqlProcessorCache processor;

  public SqlTaskExecutor(SqlProcessorCache processor,
                         ExecutorService executorService) {
    super(executorService);
    this.processor = processor;
  }

  public SqlTaskExecutor(SqlProcessorCache processor,
                         ListeningExecutorService executorService) {
    super(executorService);
    this.processor = processor;
  }

  @Override
  public ResultMap queryMap(String idStatement,
                            StatementMetadata metadata,
                            Function<SqlDsl, PreparedStatement> function) {
    ListenableFuture<ResultMap> future =
        executorService.submit(
            () -> processor.queryMap(idStatement, metadata, function));

    return getFutureValue(future);
  }

  @Override
  public <T> Map<String, T> queryCast(String idStatement,
                                      StatementMetadata metaData,
                                      Class<T> type,
                                      Function<SqlDsl, PreparedStatement> function) {
    ListenableFuture<Map<String, T>> future =
        executorService.submit(
            () -> processor.queryCast(idStatement, metaData, type, function));

    return getFutureValue(future);
  }

  @Override
  public SqlObject queryFirstObject(String idStatement,
                                    StatementMetadata metaData,
                                    Function<SqlDsl, PreparedStatement> function) {
    ListenableFuture<SqlObject> future =
        executorService.submit(
            () -> processor.queryFirstObject(idStatement, metaData, function));

    return getFutureValue(future);
  }

  @Override
  public Map<String, SqlObject> queryFirstRow(String idStatement,
                                              StatementMetadata metaData,
                                              Function<SqlDsl, PreparedStatement> function) {
    ListenableFuture<Map<String, SqlObject>> future =
        executorService.submit(
            () -> processor.queryFirstRow(idStatement, metaData, function));

    return getFutureValue(future);
  }

  @Override
  public boolean queryExist(String idStatement,
                            StatementMetadata metaData,
                            Function<SqlDsl, PreparedStatement> function) {
    ListenableFuture<Boolean> future =
        executorService.submit(
            () -> processor.queryExist(idStatement, metaData, function));

    return getFutureValue(future);
  }

  @Override
  public void update(String idStatement,
                     StatementMetadata metaData,
                     Function<SqlDsl, PreparedStatement> function) {
    executorService.execute(
        () -> processor.update(idStatement, metaData, function));
  }

  @Override
  public void batch(String idStatement,
                    Function<SqlDsl, PreparedStatement> function) {
    executorService.execute(
        () -> processor.batch(idStatement, function));
  }

  @Override
  public void batch(String idStatement,
                    StatementMetadata metaData,
                    Function<SqlDsl, PreparedStatement> function) {
    executorService.execute(
        () -> processor.batch(idStatement, metaData, function));
  }

  @Override
  public Set<String> getStatementsCached() {
    return processor.getStatementsCached();
  }

  @Override
  public SqlStatementProcessor getStatementProcessor() {
    return processor.getStatementProcessor();
  }

  @Override
  public SqlDsl getDslStatements() {
    return processor.getDslStatements();
  }
}
