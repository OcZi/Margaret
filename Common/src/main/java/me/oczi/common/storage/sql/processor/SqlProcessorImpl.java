package me.oczi.common.storage.sql.processor;

import me.oczi.common.exceptions.SQLNotSafeException;
import me.oczi.common.storage.sql.datasource.DataSource;
import me.oczi.common.storage.sql.dsl.result.ResultMap;
import me.oczi.common.storage.sql.dsl.result.SqlObject;
import me.oczi.common.storage.sql.dsl.result.SqlObjectImpl;
import me.oczi.common.utils.CommonsUtils;
import me.oczi.common.utils.Sqls;

import java.sql.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import static me.oczi.common.utils.Sqls.setObjectsStatement;

public class SqlProcessorImpl implements SqlProcessor {
  private final AtomicBoolean nullSafe;
  private final DataSource dataSource;

  private Logger logger;
  private AtomicInteger statementCount;

  public SqlProcessorImpl(DataSource dataSource) {
    this(false, dataSource);
  }

  public SqlProcessorImpl(boolean nullSafe, DataSource dataSource) {
    this(nullSafe, dataSource, null);
  }

  public SqlProcessorImpl(boolean nullSafe,
                          DataSource dataSource,
                          Logger logger) {
    this.nullSafe = new AtomicBoolean(nullSafe);
    this.dataSource = dataSource;
    this.logger = logger;
    if (logger != null) {
      this.statementCount = new AtomicInteger();
    }
  }

  public SqlObject queryFirstObject(String expression,
                                    List<Object> params) {
    logStatement(expression, params);
    try (final Connection connection = dataSource.getConnection()) {
      try (final PreparedStatement statement = connection
          .prepareStatement(expression)) {
        setObjectsStatement(statement, params, nullSafe.get());
        try (final ResultSet rs = statement.executeQuery()) {
          // Creating object before return to avoid rs.close().
          SqlObject sqlObject = null;
          while (rs.next()) {
            sqlObject = new SqlObjectImpl(1, rs);
          }
          return sqlObject == null
              ? new SqlObjectImpl()
              : sqlObject;
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return null;
  }

  @Override
  public SqlObject queryFirstObject(String expression, Object... params) {
    return queryFirstObject(expression, Arrays.asList(params));
  }

  @Override
  public Map<String, SqlObject> queryFirstRow(String expression, List<Object> params) {
    logStatement(expression, params);
    try (final Connection connection = dataSource.getConnection()) {
      try (final PreparedStatement statement = connection
          .prepareStatement(expression)) {
        setObjectsStatement(statement, params, nullSafe.get());
        try (final ResultSet rs = statement.executeQuery()) {
          ResultMap resultMap = Sqls.newResultMap(rs);
          return resultMap.isEmpty()
              ? new HashMap<>()
              : resultMap.get(0);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return null;
  }

  @Override
  public Map<String, SqlObject> queryFirstRow(String expression, Object... params) {
    return queryFirstRow(expression, Arrays.asList(params));
  }

  @Override
  public ResultMap queryMap(String expression,
                           List<Object> params) {
    logStatement(expression, params);
    try (final Connection connection = dataSource.getConnection()) {
      try (final PreparedStatement statement = connection
          .prepareStatement(expression)) {
        setObjectsStatement(statement, params, nullSafe.get());
        try (final ResultSet rs = statement.executeQuery()) {
          return Sqls.newResultMap(rs);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return null;
  }

  @Override
  public boolean queryExist(String expression,
                            Object... params) {
    return queryExist(expression, Arrays.asList(params));
  }

  @Override
  public boolean queryExist(String expression,
                            List<Object> params) {
    boolean result;
    logStatement(expression, params);
    try (final Connection connection = dataSource.getConnection()) {
      try (final PreparedStatement statement = connection
          .prepareStatement(expression)) {
        setObjectsStatement(statement, params, nullSafe.get());
        try (final ResultSet rs = statement.executeQuery()) {
          // Get result before return to avoid auto-close.
          result = rs.next();
          return result;
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return false;
  }

  @Override
  public <T> Map<String, T> queryCast(String expression,
                                      Class<T> clazz,
                                      Object... params) {
    return queryCast(expression, clazz, Arrays.asList(params));
  }

  @Override
  public <T> Map<String, T> queryCast(String expression,
                                      Class<T> clazz,
                                      List<Object> params) {
    Map<String, T> result = null;
    logStatement(expression, params);
    try (final Connection connection = dataSource.getConnection()) {
      try (final PreparedStatement statement = connection
          .prepareStatement(expression)) {
        setObjectsStatement(statement, params, nullSafe.get());
        try (final ResultSet rs = statement.executeQuery()) {
          result = Sqls.mapResultSetWithType(rs, clazz);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return result != null
        ? result
        : Collections.emptyMap();
  }

  @Override
  public ResultMap queryMap(String expression, Object... params) {
    return queryMap(expression, Arrays.asList(params));
  }

  @Override
  public void update(String expression,
                     List<Object> params) {
    logStatement(expression, params);
    try {
      try (final Connection connection = dataSource.getConnection()) {
        try (final PreparedStatement statement =
                 connection.prepareStatement(expression)) {
          setObjectsStatement(statement, params, nullSafe.get());
          statement.executeUpdate();
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void update(String expression, Object... params) {
    update(expression, Arrays.asList(params));
  }

  @Override
  public void batch(String... expressions) {
    batch(Arrays.asList(expressions));
  }

  @Override
  public void batch(List<String> expression) {
    logBatch(expression);
    try {
      try (final Connection connection = dataSource.getConnection()) {
        try (final Statement statement = connection.createStatement()) {
          for (String s : expression)  {
            statement.addBatch(s);
          }
          statement.executeBatch();
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void largeBatch(String... expression) {
    largeBatch(Arrays.asList(expression));
  }

  @Override
  public void largeBatch(List<String> expression) {
    logBatch(expression);
    try {
      try (final Connection connection = dataSource.getConnection()) {
        try (final Statement statement = connection.createStatement()) {
          for (String s : expression)  {
            statement.addBatch(s);
          }
          statement.executeLargeBatch();
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void reusableBatch(String expression,
                            List<Object> totalParameters,
                            int batches,
                            int paramSize) {
    logReusableBatch(expression, totalParameters);
    try {
      if (totalParameters.size() != (batches * paramSize)) {
        throw new SQLNotSafeException(
            "Total parameters not satisfy needed " +
                "parameters for eazh expression " +
                "(Size: " + totalParameters.size() +
                ", Needed: " + (batches * paramSize) + ")");
      }
      List<List<Object>> expParameters = CommonsUtils
          .partitionList(totalParameters, paramSize);
      try (final Connection connection = dataSource.getConnection()) {
        try (final PreparedStatement statement =
                 connection.prepareStatement(expression)) {
          for (List<Object> expParameter : expParameters) {
            setObjectsStatement(statement, expParameter);
            statement.addBatch();
          }
          statement.executeBatch();
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void reusableLargeBatch(String expression,
                                 List<Object> totalParameters,
                                 int batches,
                                 int paramSize) {
    logReusableBatch(expression, totalParameters);
    try {
      if (totalParameters.size() != (batches * paramSize)) {
        throw new SQLNotSafeException(
            "Total parameters not satisfy needed " +
                "parameters for eazh expression " +
                "(Size: " + totalParameters.size() +
                ", Needed: " + (batches * paramSize) + ")");
      }
      List<List<Object>> expParameters = CommonsUtils
          .partitionList(totalParameters, paramSize);
      try (final Connection connection = dataSource.getConnection()) {
        try (final PreparedStatement statement =
                 connection.prepareStatement(expression)) {
          for (List<Object> expParameter : expParameters) {
            setObjectsStatement(statement, expParameter);
            statement.addBatch();
          }
          statement.executeLargeBatch();
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void logStatement(String expression, List<Object> params) {
    if (logger == null) {
      return;
    }
    logStatement(expression);
    logger.info("Statament " + statementCount.getAndIncrement() + " params:" + params.toString());
  }

  private void logReusableBatch(String expression, List<Object> batches) {
    if (logger == null) {
      return;
    }
    logger.info("Statament " + statementCount.getAndIncrement() +
        " reusable batch " + expression);
    logger.info("params:" + batches.toString());
  }

  private void logBatch(List<String> batches) {
    if (logger == null) {
      return;
    }
    logger.info("Statement " + statementCount.getAndIncrement() + " batch:");
    for (String batch : batches) {
      logger.info(batch);
    }
  }

  private void logStatement(String expression) {
    if (logger == null) {
      return;
    }
    logger.info("Statament " + statementCount.get() + " expression:" + expression);
  }
}
