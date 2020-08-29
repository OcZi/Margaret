package me.oczi.common.storage.sql.processor;

import me.oczi.common.exceptions.SQLNotSafeException;
import me.oczi.common.storage.sql.datasource.DataSource;
import me.oczi.common.storage.sql.orm.result.QueryMap;
import me.oczi.common.storage.sql.orm.result.ResultMap;
import me.oczi.common.storage.sql.orm.result.SqlObject;
import me.oczi.common.storage.sql.orm.result.SqlObjectImpl;
import me.oczi.common.utils.CommonsUtils;
import me.oczi.common.utils.Sqls;

import java.sql.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static me.oczi.common.utils.Sqls.setObjectsStatement;

public class SqlProcessorImpl implements SqlProcessor {
  private final AtomicBoolean nullSafe;
  private final DataSource dataSource;

  private final AtomicInteger statementCount = new AtomicInteger();

  public SqlProcessorImpl(DataSource dataSource) {
    this(new AtomicBoolean(false), dataSource);
  }

  public SqlProcessorImpl(boolean nullSafe, DataSource dataSource) {
    this(new AtomicBoolean(nullSafe), dataSource);
  }

  public SqlProcessorImpl(AtomicBoolean nullSafe, DataSource dataSource) {
    this.nullSafe = nullSafe;
    this.dataSource = dataSource;
  }

  public SqlObject queryFirst(String expression,
                              List<Object> params) {

    // TEST
    printStatementPackage(expression, params);
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
  public SqlObject queryFirst(String expression, Object... params) {
    return queryFirst(expression, Arrays.asList(params));
  }

  @Override
  public ResultMap queryMap(String expression,
                           List<Object> params) {

    // TEST
    printStatementPackage(expression, params);
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
    // TEST
    printStatementPackage(expression, params);
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

    // TEST
    printStatementPackage(expression, params);
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

    // TEST
    printStatementPackage(expression, params);
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

    // TEST
    printBatchStatements(expression);
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
    printBatchStatements(expression);
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
    printBatchStatements(expression, totalParameters);
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
    printBatchStatements(expression, totalParameters);
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

  // TEST
  private void printStatementPackage(String expression, List<Object> params) {
    printStatementPackage(expression);
    System.out.println("Statament " + statementCount.getAndIncrement() + " params:" + params.toString());
  }

  private void printBatchStatements(String expression, List<Object> batches) {
    System.out.println("Statament " + statementCount.getAndIncrement() +
        " reusable batch " + expression);
    System.out.println("params:" + batches.toString());
  }

  private void printBatchStatements(List<String> batches) {
    System.out.println("Statement " + statementCount.getAndIncrement() + " batch:");
    batches.forEach(System.out::println);
  }

  private void printStatementPackage(String expression) {
    System.out.println("Statament " + statementCount.get() + " expression:" + expression);
  }
}
