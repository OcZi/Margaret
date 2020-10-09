package me.oczi.bukkit.internal.database.sql;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ListeningExecutorService;
import me.oczi.bukkit.internal.database.DatabaseManager;
import me.oczi.bukkit.internal.database.DbTasks;
import me.oczi.bukkit.objects.Home;
import me.oczi.bukkit.objects.id.ID;
import me.oczi.bukkit.objects.id.PartnerID;
import me.oczi.bukkit.objects.partner.Partner;
import me.oczi.bukkit.objects.partner.PartnerProperties;
import me.oczi.bukkit.objects.player.PlayerData;
import me.oczi.bukkit.other.exceptions.IdCollisionException;
import me.oczi.bukkit.utils.settings.PlayerSettings;
import me.oczi.common.executors.SqlTaskExecutor;
import me.oczi.common.storage.sql.dsl.result.ResultMap;
import me.oczi.common.storage.sql.dsl.result.SqlObject;
import me.oczi.common.storage.sql.dsl.statements.data.StatementBasicData;
import me.oczi.common.storage.sql.processor.SqlProcessorCache;
import me.oczi.common.utils.CommonsUtils;
import org.apache.commons.lang.time.DateUtils;
import org.bukkit.Location;

import java.sql.Date;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static me.oczi.bukkit.internal.database.sql.MargaretSqlTable.*;
import static me.oczi.common.storage.sql.dsl.expressions.select.SelectStatementFunction.COUNT;

public class SqlTask implements DbTasks {
  private final Date cacheDay;
  private final DatabaseManager databaseManager;
  private final SqlTaskExecutor sqlExecutor;

  public SqlTask(DatabaseManager databaseManager,
                 SqlProcessorCache executor,
                 ListeningExecutorService executorService){
    this.databaseManager = databaseManager;
    this.sqlExecutor = new SqlTaskExecutor(
        executor, executorService);
    this.cacheDay = createSqlDate();
  }

  @Override
  public void setupPlayerData(PlayerData playerData, int days) {
    String uuid = playerData.getUniqueId().toString();
    String name = playerData.getName();
    String partnerId = playerData.getPartner().getId();
    String gender = playerData.getGender().getRealName();

    List<Object> params = Lists.newArrayList(
        uuid, name, partnerId, gender, createSqlDate());

    insertValues("player-data-insert", PLAYER_DATA, params);
  }

  @Override
  public void setupPlayerSettings(UUID id, List<Object> defaultValues) {
    List<Object> params = Lists.newArrayList(id.toString());
    params.addAll(defaultValues);
    insertValues("player-settings-insert", PLAYER_SETTINGS, params);
  }

  @Override
  public void setupPartnerData(Partner partner) {
    String id = partner.getId();
    String uuid1 = partner.getUuid1().toString();
    String uuid2 = partner.getUuid2().toString();
    String relation = partner.getRelation();
    Date date = createSqlDate();

    List<Object> params = Lists.newArrayList(
        id, uuid1, uuid2, relation, date);

    insertValues("partner-data-insert", PARTNER_DATA, params);
  }

  @Override
  public void setupPartnerProperties(String id,
                                     PartnerProperties properties) {
    List<Object> params = Lists.newArrayList(id,
        properties.getHomeList().getMaxHomes(),
        properties.getBitsOfPermissions());
    insertValues("partner-properties-insert", PARTNER_PROPERTIES, params);
  }

  @Override
  public void setupPartnerHome(String partnerId, Home home) {
    String id = home.getId();
    String alias = CommonsUtils.isNullOrEmpty(home.getAlias())
        ? "unknown-" + home.getId()
        : home.getAlias();

    Location location = home.getLocation();
    String world = location.getWorld().getName();
    double x = location.getX();
    double y = location.getY();
    double z = location.getZ();
    Date date = createSqlDate();

    List<Object> params = Lists.newArrayList(
        id, alias, partnerId,
        world, x, y, z, date);
    insertValues(partnerId, PARTNER_HOME, params);
  }

  @Override
  public void updatePlayerExpire(UUID uuid, int days) {
    updateColumnOfRow("player-expire-update",
        PLAYER_DATA,
        "last_date",
        createSqlDate(),
        uuid.toString());
  }

  @Override
  public void updatePlayerData(String columnName,
                               Object param,
                               Object uuid) {
    updateColumnOfRow("", // No cache
        PLAYER_DATA,
        columnName,
        param,
        uuid.toString());
  }

  @Override
  public void updateDoublePlayerData(String columnName,
                                     Object param,
                                     Object uuid1,
                                     Object uuid2) {
    updateColumnOfRow("", // No cache
        PLAYER_DATA,
        columnName,
        param,
        uuid1.toString(),
        uuid2.toString());
  }

  @Override
  public void updatePlayerSetting(String settingName,
                                  Object param,
                                  Object uuid) {
    updateColumnOfRow("", // No cache
        PLAYER_SETTINGS,
        settingName,
        param,
        uuid.toString());
  }

  @Override
  public void updatePartnerData(String columnName,
                                Object param,
                                String id) {
    updateColumnOfRow("", // No cache
        PARTNER_DATA,
        columnName,
        param,
        id);
  }

  @Override
  public void updatePartnerProperty(String settingName,
                                    Object param,
                                    String id) {
    updateColumnOfRow("", // No cache
        PARTNER_PROPERTIES,
        settingName,
        param,
        id);
  }

  @Override
  public void setHomeList(String id, List<Object> params) {
    insertOrReplaceRow("partner-home-merge",
        PARTNER_HOMES_LIST,
        params);
  }

  @Override
  public SqlObject getColumnPlayerData(UUID uuid, String columnName) {
    return getColumnOfRow("", // No cache
        PLAYER_DATA,
        columnName,
        uuid.toString());
  }

  @Override
  public Map<String, SqlObject> getPlayerData(UUID uuid) {
    return getRow("player-data-get",
        PLAYER_DATA,
        uuid.toString(),
        Collections.singletonList("*"));
  }

  public ResultMap getAllPlayerData(String... uuids) {
    return getAllPlayerData(Arrays.asList(uuids));
  }

  // No register this statement in cache
  // because the parameters cannot be predicted.
  @Override
  public ResultMap getAllPlayerData(List<String> uuids) {
    StatementBasicData data = StatementBasicData
        .newData(PLAYER_DATA,
            Arrays.asList(
                "id, name, gender, partnerid, last_date",
                "id"),
            uuids);
    return sqlExecutor
        .queryMap("", // Empty statement id to bypass cache
            data,
            dsl -> dsl.select("columns")
                .from(PLAYER_DATA)
                .where("id", uuids)
                .build());
  }

  @Override
  public Date getPlayerExpire(UUID uuid) {
    return getColumnOfRow("player-expire-get",
        PLAYER_DATA,
        "expire",
        uuid.toString())
        .getSqlDateOrDefault(null);
  }

  @Override
  public Map<String, SqlObject> getPlayerSettings(UUID uuid) {
    List<String> settings = Lists.newArrayList(
        PlayerSettings.getDatabaseSettings().keySet());
    return getRow("player-settings-get",
        PLAYER_SETTINGS,
        uuid.toString(),
        settings);
  }

  @Override
  public boolean getPlayerSetting(UUID uuid,
                                  String settingName) {
    return getColumnOfRow("player-setting-get",
        PLAYER_SETTINGS,
        settingName,
        uuid.toString())
        .getBooleanOrDefault(false);
  }

  @Override
  public ResultMap getTopOfPartners(int limit) {
    return getRowsByColumn("",
        PARTNER_DATA,
        limit,
        "creation_date",
        Lists.newArrayList("id", "player1", "player2"));
  }

  @Override
  public int getCountOfPartners() {
    StatementBasicData data = StatementBasicData.newData(
        PARTNER_DATA,
        null,
        null);
    return sqlExecutor.queryFirstObject("partner-count-get",
        data,
        dsl -> dsl.select(COUNT)
            .from(PARTNER_DATA)
            .build()).getInteger();
  }

  @Override
  public Map<String, SqlObject> getPartnerData(String id) {
    return getRow("partner-data-get",
        PARTNER_DATA,
        id,
        Collections.singletonList("*"));
  }

  @Override
  public ResultMap getAnythingOfPartnerData() {
    StatementBasicData data = StatementBasicData
        .newData(PLAYER_DATA,
            Arrays.asList("id", "player1", "player2"),
            null);
    return sqlExecutor
        .queryMap("", // Empty statement id to bypass cache
            data,
            dsl -> dsl.select("*")
                .from(PARTNER_DATA)
                .build());
  }

  @Override
  public Map<String, SqlObject> getPartnerHomeList(String id) {
    List<String> columns = Lists.newArrayList(databaseManager.getTableHomesId());
    return getRow("partner-home-list-get",
        PARTNER_HOMES_LIST,
        id,
        columns);
  }

  @Override
  public Map<String, SqlObject> getHome(String id) {
    return getRow("partner-home-get",
        PARTNER_HOME,
        id,
        Collections.singletonList("*"));
  }

  @Override
  public Map<String, SqlObject> getPartnerProperties(String id) {
    List<String> params = Lists.newArrayList(
        "bitpermissions", "maxhomes");
    return getRow("partner-properties-get",
        PARTNER_PROPERTIES,
        id,
        params);
  }

  @Override
  public void deletePlayerData(UUID uuid) {
    deleteRow("player-data-delete",
        PLAYER_DATA,
        uuid.toString());
  }

  @Override
  public void deletePlayerSettings(UUID uuid) {
    deleteRow("player-settings-delete",
        PLAYER_SETTINGS,
        uuid.toString());
  }

  @Override
  public void deletePartnerData(Partner partner) {
    deletePartnerData(partner.getId());
  }

  @Override
  public void deletePartnerData(String id) {
    deleteRow("partner-data-delete", PARTNER_DATA, id);
  }


  @Override
  public void deletePartnerProperties(Partner partner) {
    deletePartnerProperties(partner.getId());
  }

  @Override
  public void deletePartnerProperties(String id) {
    deleteRow("partner-properties-delete",
        PARTNER_PROPERTIES,
        id);
  }

  @Override
  public void deletePartnerHomeList(Partner partner) {
    deletePartnerHomeList(partner.getId());
  }

  @Override
  public void deletePartnerHomeList(String id) {
    deleteRow("partner-homes-delete",
        PARTNER_HOME,
        id);
    deleteRow("partner-home-list-delete",
        PARTNER_HOMES_LIST,
        id);
  }

  @Override
  public void deletePartnerHome(String id) {
    deleteRow("partner-home-delete", PARTNER_HOME, id);
  }

  @Override
  public boolean playerDataExist(UUID uuid) {
    return rowExist("player-data-exist",
        PLAYER_DATA,
        uuid.toString());
  }

  @Override
  public boolean playerSettingsExist(UUID uuid) {
    return rowExist("partner-settings-exist",
        PLAYER_SETTINGS,
        uuid.toString());
  }

  @Override
  public boolean partnerDataExist(String id) {
    return rowExist("partner-data-exist", PARTNER_DATA, id);
  }

  @Override
  public boolean partnerHomeListExist(String id) {
    return rowExist("partner-home-list-exist", PARTNER_HOMES_LIST, id);
  }

  @Override
  public boolean partnerPropertiesExist(String id) {
    return rowExist("partner-properties-exist", PARTNER_PROPERTIES, id);
  }

  @Override
  public boolean partnerHomeExist(String id) {
    return rowExist("partner-home-exist", PARTNER_HOME, id);
  }

  private void insertValues(String id,
                            MargaretSqlTable table,
                            List<Object> params) {
    createRow(id, table, null, params);
  }

  private void createRow(String id,
                         MargaretSqlTable table,
                         List<String> columns,
                         List<Object> params) {
    StatementBasicData data = StatementBasicData
        .newData(table, columns, params);
    sqlExecutor.update(id,
        data,
        dsl -> dsl.insert()
            .into(table)
            .values(params)
            .build());
  }

  private ResultMap getRows(String idStatement,
                            MargaretSqlTable table,
                            Object id,
                            List<String> selects) {
    List<String> columns  = Lists.newArrayList(
        String.join(", ", selects));
    columns.add("id");
    StatementBasicData data = StatementBasicData.newData(
        table, columns, Collections.singletonList(id));
    return sqlExecutor.queryMap(idStatement, data,
        dsl -> dsl.select(selects)
            .from(table)
            .where("id", id)
            .build());
  }

  private Map<String, SqlObject> getRow(String idStatement,
                            MargaretSqlTable table,
                            Object id,
                            List<String> selects) {
    List<String> columns  = Lists.newArrayList(
        String.join(", ", selects));
    columns.add("id");
    StatementBasicData data = StatementBasicData.newData(
        table, columns, Collections.singletonList(id));
    return sqlExecutor.queryFirstRow(idStatement, data,
        dsl -> dsl.select(selects)
            .from(table)
            .where("id", id)
            .build());
  }

  private SqlObject getColumnOfRow(String idStatement,
                                   MargaretSqlTable table,
                                   String columnName,
                                   String id) {
    StatementBasicData data = StatementBasicData.newData(table,
        Lists.newArrayList(columnName, "id"),
        Lists.newArrayList(id));
    return sqlExecutor.queryFirstObject(idStatement, data,
        dsl -> dsl.select(columnName)
            .from(table)
            .where("id", id)
            .build());
  }

  private ResultMap getFixedRows(String idStatement,
                                 MargaretSqlTable table,
                                 int start,
                                 int end,
                                 List<String> selects) {
    List<String> columns  = Lists.newArrayList(
        String.join(", ", selects));
    StatementBasicData data = StatementBasicData.newData(
        table, columns, null);
    return sqlExecutor.queryMap(idStatement, data,
        dsl -> dsl.select(selects)
            .from(table)
            .offset(start)
            .limit(end)
            .build());
  }

  private ResultMap getFirstRows(String idStatement,
                                 MargaretSqlTable table,
                                 int limit,
                                 List<String> selects) {
    List<String> columns  = Lists.newArrayList(
        String.join(", ", selects));
    StatementBasicData data = StatementBasicData.newData(
        table, columns, null);
    return sqlExecutor.queryMap(idStatement, data,
        dsl -> dsl.select(columns)
            .from(table)
            .limit(limit)
            .build());
  }

  private ResultMap getRowsByColumn(String idStatement,
                                    MargaretSqlTable table,
                                    int limit,
                                    String orderColumnName,
                                    List<String> selects) {
    List<String> columns  = Lists.newArrayList(
        String.join(", ", selects));
    columns.add(orderColumnName);
    StatementBasicData data = StatementBasicData.newData(
        table, columns, null);
    return sqlExecutor.queryMap(idStatement, data,
        dsl -> dsl.select(columns)
            .from(table)
            .orderBy(orderColumnName)
            .limit(limit)
            .build());
  }

  private void insertOrReplaceRow(String idStatement,
                                  MargaretSqlTable table,
                                  List<?> params) {
    StatementBasicData data = StatementBasicData.newData(table,
        Collections.emptyList(), params);
    sqlExecutor.update(idStatement, data,
        dsl -> dsl.insert()
            .orReplace()
            .into(table)
            .values(params)
            .build());
  }

  private void updateColumnOfRow(String idStatement,
                                 MargaretSqlTable table,
                                 String columnName,
                                 Object param,
                                 Object... ids) {
    updateColumnOfRow(idStatement,
        table,
        columnName,
        param,
        Arrays.asList(ids));
  }

  /**
   * Update a specified column of row.
   * @param table - SQL table
   * @param columnName - Name of column to update
   * @param param - Update parameter for columnName
   * @param ids - ID of row
   */
  private void updateColumnOfRow(String idStatement,
                                 MargaretSqlTable table,
                                 String columnName,
                                 Object param,
                                 List<Object> ids) {
    List<Object> params = Lists.newArrayList(param);
    params.addAll(ids);
    StatementBasicData data = StatementBasicData.newData(table,
        Lists.newArrayList(columnName, "id"),
        params);
    sqlExecutor.update(idStatement, data,
        dsl -> dsl.update(table)
            .set(columnName, param)
            .where("id", ids)
            .build());
  }

  private void deleteRow(String idStatement,
                         MargaretSqlTable table,
                         String id) {
    StatementBasicData data = StatementBasicData.newData(table,
        Lists.newArrayList("id"), Lists.newArrayList(id));
    sqlExecutor.update(idStatement, data,
        dsl -> dsl.deleteFrom(table)
            .where("id", id)
            .build());
  }

  private boolean rowExist(String idStatement,
                           MargaretSqlTable table,
                           Object id) {
    StatementBasicData data = StatementBasicData.newData(table,
        Lists.newArrayList("id", "id"),
        Lists.newArrayList(id));
    return sqlExecutor.queryExist(idStatement, data,
        dsl -> dsl.select("id")
            .from(table)
            .where("id", id)
            .limit(1)
            .build());
  }

  @Override
  public String foundUnusedPartnerId() {
    return foundUnusedId(new PartnerID());
  }

  @Override
  public String foundUnusedId(ID id) {
    AtomicInteger trying = new AtomicInteger();
    while (true) {
      if (trying.get() >= 5) {
        throw new IdCollisionException
            ("Too much colliisions of id " + id.getID());
      }
      id.generateNewId();
      if (!rowExist("partner-exist", PARTNER_DATA, id.getID())) {
        break;
      }
      trying.getAndIncrement();
    }

    return id.getID();
  }

  public Date createSqlDate() {
    if (cacheDay == null ||
        !DateUtils.isSameDay(cacheDay,
            new Date(System.currentTimeMillis()))) {
      return new Date(System.currentTimeMillis());
    }

    return cacheDay;
  }

  public SqlTaskExecutor getSqlExecutor() {
    return sqlExecutor;
  }
}
