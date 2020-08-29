package me.oczi.bukkit.internal.database.sql;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import me.oczi.bukkit.MargaretMain;
import me.oczi.bukkit.internal.database.DbTasks;
import me.oczi.bukkit.storage.yaml.MargaretYamlStorage;
import me.oczi.bukkit.storage.yaml.impl.DataSourceConfigImpl;
import me.oczi.bukkit.storage.yaml.impl.DataSourcePropertiesImpl;
import me.oczi.bukkit.storage.yaml.impl.HkDataSourceConfigImpl;
import me.oczi.bukkit.utils.MessageUtils;
import me.oczi.common.api.configuration.DataSourceConfig;
import me.oczi.common.api.configuration.DataSourceProperties;
import me.oczi.common.api.configuration.HkDataSourceConfig;
import me.oczi.common.storage.sql.datasource.DataSource;
import me.oczi.common.storage.sql.datasource.DataSourceType;
import me.oczi.common.storage.sql.datasource.DataSourceTypePackage;
import me.oczi.common.storage.sql.datasource.instance.DataSourceCPBuilder;
import me.oczi.common.storage.sql.datasource.instance.DataSources;
import me.oczi.common.storage.sql.orm.dsl.SqlDsl;
import me.oczi.common.storage.sql.orm.dsl.SqlDslImpl;
import me.oczi.common.storage.sql.orm.statements.prepared.PreparedStatement;
import me.oczi.common.storage.sql.processor.*;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SqlManagerImpl implements SqlManager {
  private SqlTask sqlTask;
  private DataSource dataSource;

  private final SqlDsl dsl;
  private SqlStatementProcessor adapter;

  private SqlProcessorCache executor;
  private DbScript script;

  private int threads = 1;
  private int maxPossibleHomes;

  private DataSourceType dataSourceType;

  private List<String> homesId;

  public SqlManagerImpl() {
    checkDataSourceType();
    this.dsl = new SqlDslImpl(this.dataSourceType);
  }

  @Override
  public void init() {
    this.maxPossibleHomes = MargaretYamlStorage
        .getMaxPossibleHomes();
    this.dataSource = createDataSource();
    SqlProcessor processor = new SqlProcessorImpl(true, dataSource);
    this.adapter = new SqlStatementProcessorImpl(processor);
    this.script = new SqlScript(this,
        dsl,
        adapter,
        maxPossibleHomes,
        MargaretYamlStorage.getDaysToExpire());

    this.executor = newExecutor();
    this.sqlTask = createTaskExecutor();
  }

  private SqlProcessorCache newExecutor() {
    Cache<String, PreparedStatement> cache = Caffeine
        .newBuilder()
        .expireAfterWrite(6, TimeUnit.MINUTES)
        .build();
    return new SqlProcessorCacheImpl(cache.asMap(),
        dsl,
        adapter);
  }

  @Override
  public void scriptInit() {
    script.init();
  }

  private SqlTask createTaskExecutor() {
    return new SqlTask(this,
        executor,
        createListeningExecutorService());
  }

  private DataSource createDataSource() {
    FileConfiguration config = MargaretYamlStorage
        .getDatabaseConfig().getAccess();
    DataSourceConfig dsConfig =
        new DataSourceConfigImpl(config);
    HkDataSourceConfig hkConfig =
        new HkDataSourceConfigImpl(config);
    DataSourceProperties properties =
        new DataSourcePropertiesImpl(
            config, this.dataSourceType);

    DataSourceTypePackage dataSourceTypePackage =
        new DataSourceTypePackage(dsConfig, this.dataSourceType);
    String jdbcUrl = buildJdbcUrl(dataSourceTypePackage, dsConfig);
    DataSourceCPBuilder builder = DataSources.newDataSourceCP(
        dataSourceTypePackage, jdbcUrl)
        .setProperties(properties)
        .setConfig(hkConfig);
    builder = setCredentialsIfNecessary(
        builder, dsConfig);
    return builder.build();
  }

  private DataSourceCPBuilder setCredentialsIfNecessary(DataSourceCPBuilder builder,
                                                        DataSourceConfig dsConfig) {
    String databaseMode = DataSourceType.checkMode(
        dsConfig.getMode(), this.dataSourceType);
    return databaseMode.equalsIgnoreCase("server")
        ? builder.setCredentials(dsConfig)
        : builder;
  }

  private ListeningExecutorService createListeningExecutorService() {
    return MoreExecutors
        .listeningDecorator(createExecutorService());
  }

  private ExecutorService createExecutorService() {
    this.threads = MargaretYamlStorage.getThreads();
    if (threads > 1) {
      int processors = Runtime.getRuntime().availableProcessors();
      if (threads > processors) {
        MessageUtils.warning("Defined threads for database are" +
                " more that available processors " +
                "(Threads: " + threads + ", " +
                "Processors: " + processors + ")",
            "Set Database threads to 1.");
        this.threads = 1;
        return Executors.newSingleThreadExecutor();
      }
      return Executors.newFixedThreadPool(this.threads);
    } else if (threads == 1){
      return Executors.newSingleThreadExecutor();
    } else {
      return Executors.newCachedThreadPool();
    }
  }

  private void checkDataSourceType() {
    String type = MargaretYamlStorage.getDatabaseType();
    try {
      this.dataSourceType = DataSourceType
          .valueOf(type.toUpperCase());
    } catch (IllegalArgumentException ignored) {
      MessageUtils.warning(
          "DataSource " + type
          + " is invalid. Set to SQLite.");
      dataSourceType = DataSourceType.SQLITE;
    }
  }

  public String buildJdbcUrl(DataSourceTypePackage dataSourceTypePackage,
                             DataSourceConfig config) {
    File dataFolder = MargaretMain
        .getCore()
        .getDataFolder();
    return DataSources.newJdbcUrlBuilder(dataSourceTypePackage)
        .setServerMode(config)
        .setEmbeddedMode(dataFolder)
        .build();
  }

  @Override
  public DbTasks getDatabaseTask() {
    return sqlTask;
  }

  @Override
  public String getDatabaseTypeName() {
    return dataSourceType.toString().toLowerCase();
  }

  @Override
  public DataSourceType getDatabaseType() {
    return dataSourceType;
  }

  @Override
  public int getThreadsUsed() {
    return threads;
  }

  @Override
  public void shutdown() {
    sqlTask.getSqlExecutor().close();
    dataSource.close();
  }

  @Override
  public int getMaxPossibleHomes() {
    return maxPossibleHomes;
  }

  @Override
  public void createTableHomesId() {
    createTableHomesId(maxPossibleHomes);
  }

  public void setMaxPossibleHomes(int maxPossibleHomes) {
    this.maxPossibleHomes = maxPossibleHomes;
  }

  @Override
  public void createTableHomesId(int size) {
    List<String> list = new ArrayList<>();
    for (int i = 0; i < size; i++) {
      list.add(String.format("homes_id_%s", i + 1));
    }
    homesId = list;
  }

  @Override
  public List<String> getTableHomesId() {
    if (homesId == null) {
      createTableHomesId();
    }
    return homesId;
  }
}
