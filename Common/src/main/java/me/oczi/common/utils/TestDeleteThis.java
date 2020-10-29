package me.oczi.common.utils;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.collect.Lists;
import me.oczi.common.api.configuration.DataSourceConfig;
import me.oczi.common.api.configuration.HkDataSourceConfig;
import me.oczi.common.api.github.GithubRelease;
import me.oczi.common.api.mojang.HistoryNameEntry;
import me.oczi.common.api.mojang.MojangAccount;
import me.oczi.common.api.sql.SqlTable;
import me.oczi.common.exceptions.SQLCastException;
import me.oczi.common.executors.SqlTaskExecutor;
import me.oczi.common.request.github.GithubReleaseResolver;
import me.oczi.common.request.mojang.AsyncMojangResolver;
import me.oczi.common.request.mojang.MojangResolver;
import me.oczi.common.storage.sql.datasource.DataSource;
import me.oczi.common.storage.sql.datasource.DataSourceType;
import me.oczi.common.storage.sql.datasource.DataSourceTypePackage;
import me.oczi.common.storage.sql.datasource.instance.DataSources;
import me.oczi.common.storage.sql.dsl.expressions.SqlDsl;
import me.oczi.common.storage.sql.dsl.expressions.SqlDslImpl;
import me.oczi.common.storage.sql.dsl.expressions.clause.OrderPattern;
import me.oczi.common.storage.sql.dsl.expressions.select.SelectStatementFunction;
import me.oczi.common.storage.sql.dsl.result.ResultMap;
import me.oczi.common.storage.sql.dsl.result.SqlObject;
import me.oczi.common.storage.sql.dsl.statements.data.StatementBasicData;
import me.oczi.common.storage.sql.dsl.statements.data.StatementBasicDataImpl;
import me.oczi.common.storage.sql.dsl.statements.prepared.PreparedStatement;
import me.oczi.common.storage.sql.interoperability.ConstraintsComp;
import me.oczi.common.storage.sql.interoperability.SqlConstraints;
import me.oczi.common.storage.sql.processor.*;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 * Class test in src.
 * Used because JUnit is bugged for me.
 */
public class TestDeleteThis {
  public static final AtomicInteger ints = new AtomicInteger();
  public static final int random1 = new Random().nextInt(4000);
  public static final int random2 = new Random().nextInt(4000);
  public static final SqlDsl dsl = new SqlDslImpl(DataSourceType.H2);
  public static SqlProcessorCache master;

  public static final Function<SqlDsl, PreparedStatement> varr = dao -> dao
      .createTable(TestTables.TEST)
      .ifNotExist()
      .build();

  public static final Function<SqlDsl, PreparedStatement> insert1 = dao -> dao
      .insert()
      .into(TestTables.TEST)
      .values(random1, random2)
      .build();

  public static final Function<SqlDsl, PreparedStatement> select1 = dao -> dao
      .select("*")
      .from(TestTables.TEST)
      .where("id", random1)
      .build();

  public static final Function<SqlDsl, PreparedStatement> select2 = dao -> dao
      .select("*")
      .from(TestTables.TEST)
      .build();

  public static final Function<SqlDsl, PreparedStatement> select3 = dao -> dao
      .select("id")
      .from(TestTables.TEST)
      .where("int2", random2)
      .build();

  public static final Function<SqlDsl, PreparedStatement> select_all = dao -> dao
      .select(SelectStatementFunction.COUNT)
      .from(TestTables.TEST)
      .build();

  public static void main(String[] args)
      throws InterruptedException,
      ExecutionException,
      TimeoutException, IOException {
    testResolver();
    System.out.println("Select:");
    System.out.println("1:" + dsl
        .select("nani", "nani")
        .from(TestTables.TEST)
        .where("nani", 1, 2, 3)
        .build()
        .toString());
    System.out.println("2:" + dsl
        .select("nani", "nani")
        .from(TestTables.TEST)
        .build()
        .toString());
    System.out.println("3:" + dsl
        .select("partnerid", "gender")
        .from(TestTables.TEST)
        .build()
        .toString());
    System.out.println("4:" + dsl
        .select("partnerid")
        .from(TestTables.TEST)
        .orderBy("partnerid")
        .limit(1)
        .build()
        .toString());
    System.out.println("5:" + dsl
        .select("nani", 3, true, 1L, TestTables.TEST, 'a', 3.2)
        .from(TestTables.TEST)
        .orderBy("partnerid")
        .limit(1)
        .build()
        .toString());
    System.out.println("6:" + dsl
        .select(SelectStatementFunction.COUNT)
        .from(TestTables.TEST)
        .where("nani", 1, 2, 3)
        .build()
        .toString());
    System.out.println("7:" + dsl
        .select("nani", 3, true, 1L, TestTables.TEST, 'a', 3.2)
        .from(TestTables.TEST)
        .orderRecursive(r ->
            r.orderBy("now")
                .column("pepe")
                .column("yep", OrderPattern.ASCENDING)
                .column(new String[]{"e", "a"}, OrderPattern.ASCENDING))
        .limit(1)
        .build()
        .toString());
    System.out.println("8:" + dsl
        .select("nani", 3, true, 1L, TestTables.TEST, 'a', 3.2)
        .from(TestTables.TEST)
        .orderBy(new String[]{"id", "e"}, OrderPattern.DESCENDING)
        .limit(1)
        .build()
        .toString());
    System.out.println("9:" + dsl
        .select("nani")
        .from(TestTables.TEST)
        .offset(10)
        .limit(10)
        .build()
        .toString());

    System.out.println();
    System.out.println("Insert:");
    System.out.println("1:" + dsl
        .insert()
        .into(TestTables.TEST)
        .values("pepe", "nani")
        .toString());
    System.out.println("2:" + dsl
        .insert(DataSourceType.MYSQL)
        .orReplace()
        .into(TestTables.TEST)
        .values("nani", "pepe")
        .toString());
    System.out.println("3:" + dsl
        .insert(DataSourceType.POSTGRESQL)
        .orReplace()
        .into(TestTables.TEST)
        .values("nani", "pepe")
        .toString());
    System.out.println("4:" + dsl
        .insert(DataSourceType.H2)
        .orReplace()
        .into(TestTables.TEST)
        .values("nani", "pepe")
        .toString());
    System.out.println("5:" + dsl
        .insert(DataSourceType.SQLITE)
        .orReplace()
        .into(TestTables.TEST)
        .values("nani", "pepe")
        .toString());

    System.out.println();
    System.out.println("Update:");
    System.out.println("1:" + dsl
        .update(TestTables.TEST)
        .set("id", 2)
        .where("id", 3)
        .build()
        .toString());
    System.out.println("2:" + dsl
        .update(TestTables.COMP)
        .set("pepe", "no", "non")
        .whereNot("pepe", "maybe")
        .build()
        .toString());

    System.out.println();
    System.out.println("Delete:");
    System.out.println("1:" + dsl
        .deleteFrom(TestTables.COMP)
        .whereNot("pepe", "no")
        .build()
        .toString());
    System.out.println("2:" + dsl
        .deleteFrom(TestTables.COMP)
        .whereRecursive(recursive ->
            recursive.where("nani", 3)
                .and("nano", 2)
                .and("nandato", 1))
        .build()
        .toString());


    System.out.println();
    System.out.println("Tables:");
    System.out.println(dsl.createTable(TestTables.TEST)
        .build());
    System.out.println(dsl
        .createTable(TestTables.TEST)
        .ifNotExist()
        .build());
    System.out.println(dsl
        .createTable(TestTables.COMP, DataSourceType.SQLITE)
        .ifNotExist()
        .build());
    System.out.println(dsl
        .createTable(TestTables.COMP, DataSourceType.H2)
        .ifNotExist()
        .build());
    System.out.println(dsl
        .createTable(TestTables.COMP, DataSourceType.POSTGRESQL)
        .ifNotExist()
        .build());
    System.out.println(dsl
        .createTable(TestTables.COMP, DataSourceType.MYSQL)
        .ifNotExist()
        .build());

    try {
      Class.forName("com.zaxxer.hikari");
      System.out.println();
      System.out.println("Test:");
      initSql();
      testSql();
      System.out.println(master.getStatementsCached());
    } catch (ClassNotFoundException e) {
      System.out.println("HikariCP not enabled.");
    }
  }

  public static void initSql() {
    DataSourceConfig config = new DataSourceConfigImplTest();
    DataSourceTypePackage packageTest = new DataSourceTypePackage(config, DataSourceType.H2);
    String jdbcurl = DataSources
        .newJdbcUrlBuilder(packageTest)
        .setEmbeddedMode(new File(""))
        .build();
    DataSource dataSource = DataSources.newDataSourceCP(packageTest, jdbcurl)
        .setConfig(new HkDataSourceConfigImplTest())
        .build();
    Cache<String, PreparedStatement> cache = Caffeine.newBuilder()
        .expireAfterWrite(30, TimeUnit.SECONDS)
        .build();
    SqlStatementProcessor statementProcessor =
        new SqlStatementProcessorImpl(
            new SqlProcessorImpl(true, dataSource));
    master = new SqlProcessorCacheImpl(
        cache.asMap(),
        dsl,
        statementProcessor);
    master = new SqlTaskExecutor(master, Executors.newFixedThreadPool(1));
    master.batch("basic-batch", varr);
  }

  public static StatementBasicData getMetaDataOfFunction(
      Function<SqlDsl, PreparedStatement> function) {
    return function.apply(dsl).getModifiableData();
  }

  public static void testSql() {
    update(new StatementBasicDataImpl(
        insert1.apply(dsl).getModifiableData()), insert1);
    query(new StatementBasicDataImpl(
        select1.apply(dsl).getModifiableData()), select1);
    query(getMetaDataOfFunction(select2), select2);
    query(new StatementBasicDataImpl(
            select3.apply(dsl).getModifiableData()),
        select3);
    query(getMetaDataOfFunction(select_all), select_all);
    StatementBasicData metaData =
        new StatementBasicDataImpl(
            TestTables.TEST,
            Lists.newArrayList("*"),
            Lists.newArrayList("*"));
    Map<String, String> resultMap = master.queryCast("cast-1",
        metaData,
        String.class,
        dao -> dao
            .select("a")
            .from(null)
            .build());
    System.out.println(resultMap);
  }

  public static void query(StatementBasicData metaData,
                           Function<SqlDsl, PreparedStatement> function) {
    ResultMap map = master
        .queryMap("query-" + ints.getAndIncrement(),
            metaData,
            function);
    if (map == null) {
      return;
    }
    for (Map<String, SqlObject> row : map.getRows()) {
      if (row.isEmpty()) {
        System.out.println("row is null.");
        continue;
      }
      for (SqlObject sqlObject : row.values()) {
        if (sqlObject.isEmpty()) {
          System.out.println("sqlObject is null.");
          continue;
        }
        try {
          System.out.println(sqlObject.getInteger() + " - " +
              sqlObject.getMetadata().toString());
        } catch (SQLCastException e) {
          e.printStackTrace();
        }

      }
    }
  }

  public static void testResolver() throws InterruptedException, ExecutionException, TimeoutException, IOException {
    AsyncMojangResolver resolver = MojangResolver
        .newAsyncResolver(Executors.newSingleThreadExecutor());
    MojangAccount oczi = resolver.resolveAccount("_oczi");
    System.out.println("oczi = " + oczi);
    HistoryNameEntry[] historyName = resolver
        .resolveHistoryName(oczi.getId());
    System.out.println("historyName = " + Arrays.toString(historyName));
    resolver.shutdown();
    GithubReleaseResolver releaseResolver = GithubReleaseResolver.newResolver();
    GithubRelease[] releases = releaseResolver.getReleases("OcZi", "Margaret");
    System.out.println(Arrays.toString(releases));
    Instant parse = Instant.parse(releases[0].getPublishedDate());
    System.out.println("parse = " + parse);
    System.out.println("date = " + new Date(parse.toEpochMilli()));
  }

  public static void update(StatementBasicData metaData,
                            Function<SqlDsl, PreparedStatement> function) {
    master.update("update-" + ints.getAndIncrement(),
        metaData,
        function);
  }

  private enum TestTables implements SqlTable {
    TEST("margaret_Testo",
        "id INTEGER PRIMARY KEY",
        "int2 INTEGER"),

    @SqlConstraints(expression = ConstraintsComp.AUTO_INCREMENT)
    COMP("comp", "testme INTEGER {AUTO_INCREMENT} PRIMARY KEY", "test2 VARCHAR(255)");

    private final String name;
    private final String[] columns;


    TestTables(String name, String... columns) {
      this.name = name;
      this.columns = columns;
    }

    @Override
    public String getName() {
      return name;
    }

    @Override
    public String[] getColumns() {
      return columns;
    }
  }

  private static class DataSourceConfigImplTest implements DataSourceConfig {

    @Override
    public String getMode() {
      return "embedded";
    }

    @Override
    public boolean isServerMode() {
      return false;
    }

    @Override
    public boolean isEmbeddedMode() {
      return true;
    }

    @Override
    public String getUsername() {
      return null;
    }

    @Override
    public String getPassword() {
      return null;
    }

    @Override
    public String getDatabase() {
      return null;
    }

    @Override
    public String getHostname() {
      return null;
    }

    @Override
    public String getPort() {
      return null;
    }
  }

  private static class HkDataSourceConfigImplTest implements HkDataSourceConfig {

    @Override
    public String getConnectionTestQuery() {
      return "SELECT 1";
    }

    @Override
    public long getConnectionTimeout() {
      return 30000;
    }

    @Override
    public long getIdleTimeout() {
      return 600000;
    }

    @Override
    public long getMaxLifetime() {
      return 1800000;
    }

    @Override
    public int getMinimumIdle() {
      return 10;
    }

    @Override
    public int getMaximumPoolSize() {
      return 10;
    }
  }
}
