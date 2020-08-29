package me.oczi.common.storage.sql.processor;

import me.oczi.common.storage.sql.orm.result.ResultMap;
import me.oczi.common.storage.sql.orm.result.SqlObject;
import me.oczi.common.storage.sql.orm.dsl.SqlDsl;
import me.oczi.common.storage.sql.orm.statements.data.StatementMetadata;
import me.oczi.common.storage.sql.orm.statements.prepared.PreparedStatement;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public interface SqlProcessorCache {

  ResultMap queryMap(String idStatement,
                     StatementMetadata metaData,
                     Function<SqlDsl, PreparedStatement> function);

  <T> Map<String, T> queryCast(String idStatement,
                               StatementMetadata metaData,
                               Class<T> type,
                               Function<SqlDsl, PreparedStatement> function);

  SqlObject queryFirst(String idStatement,
                       StatementMetadata metaData,
                       Function<SqlDsl, PreparedStatement> function);

  boolean queryExist(String idStatement,
                     StatementMetadata metaData,
                     Function<SqlDsl, PreparedStatement> function);

  void update(String idStatement,
              StatementMetadata metaData,
              Function<SqlDsl, PreparedStatement> function);

  void batch(String idStatement,
             Function<SqlDsl, PreparedStatement> function);

  void batch(String idStatement,
             StatementMetadata metaData,
             Function<SqlDsl, PreparedStatement> function);

  Set<String> getStatementsCached();

  SqlStatementProcessor getProcessorAdapter();

  SqlDsl getDaoStatements();
}
