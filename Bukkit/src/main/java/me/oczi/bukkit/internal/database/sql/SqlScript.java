package me.oczi.bukkit.internal.database.sql;

import com.google.common.base.Preconditions;
import me.oczi.bukkit.utils.EmptyObjects;
import me.oczi.bukkit.utils.MessageUtils;
import me.oczi.common.api.collections.TypePair;
import me.oczi.common.api.collections.TypePairImpl;
import me.oczi.common.storage.sql.dsl.expressions.SqlDsl;
import me.oczi.common.storage.sql.dsl.result.ResultMap;
import me.oczi.common.storage.sql.dsl.result.SqlObject;
import me.oczi.common.storage.sql.dsl.statements.data.StatementBasicData;
import me.oczi.common.storage.sql.dsl.statements.prepared.PreparedStatement;
import me.oczi.common.storage.sql.processor.SqlProcessor;
import me.oczi.common.storage.sql.processor.SqlStatementProcessor;
import me.oczi.common.utils.CommonsUtils;
import org.apache.commons.lang.ArrayUtils;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SqlScript implements DbScript {
  private final SqlManagerImpl sqlManager;

  private final SqlDsl dsl;

  private final SqlStatementProcessor statementProcessor;
  private final SqlProcessor processor;

  private final int maxPossibleHomes;
  private final int daysToExpire;

  public SqlScript(SqlManagerImpl sqlManager,
                   SqlDsl dsl,
                   SqlStatementProcessor statementProcessor,
                   int maxPossibleHomes,
                   int daysToExpire) {
    this.sqlManager = sqlManager;
    this.dsl = dsl;
    this.maxPossibleHomes = maxPossibleHomes;
    this.daysToExpire = daysToExpire;

    this.statementProcessor = statementProcessor;
    this.processor = statementProcessor.getProcessor();
  }


  @Override
  public void init() {
    createEssentialsTables();
    createDynamicTables();
    deleteOutdatedEntries();
  }

  private void createEssentialsTables() {
    List<PreparedStatement> batches = new ArrayList<>(
        MargaretSqlTable.values().length);
    for (MargaretSqlTable table : MargaretSqlTable.values()) {
      if (table == MargaretSqlTable.PARTNERSHIP_HOMES_LIST) {
        continue;
      }
      batches.add(dsl
          .createTable(table)
          .ifNotExist()
          .defaultCharSet("utf8mb4")
          .build());
    }

    statementProcessor.batch(batches);
  }

  private void createDynamicTables() {
    Map<String, SqlObject> query =
        statementProcessor.queryFirstRow(dsl
            .select("value")
            .from(MargaretSqlTable.SQL_PROPERTIES)
            .where("id", "max_possible_homes")
            .build());
    int propertiesMaxHomes;
    if (CommonsUtils.isNullOrEmpty(query)) {
      propertiesMaxHomes = maxPossibleHomes;
      initHomeList();
    } else {
      propertiesMaxHomes = query.get("value")
          .getIntegerOrDefault(maxPossibleHomes);
    }
    if (maxPossibleHomes < propertiesMaxHomes) {
      MessageUtils.warning(
          "Max possible homes cannot be decremented after be modified.",
          "Ignore max homes configuration.");
      sqlManager.setMaxPossibleHomes(propertiesMaxHomes);
    } else if (maxPossibleHomes > propertiesMaxHomes) {
      int addCount = maxPossibleHomes - propertiesMaxHomes;
      alterHomeList(addCount);
    }
  }

  private void initHomeList() {
    statementProcessor.batch(dsl
        .createTable(MargaretSqlTable.PARTNERSHIP_HOMES_LIST)
        .ifNotExist()
        .defaultCharSet("utf8mb4")
        .build());
    statementProcessor.update(dsl
        .insert()
        .into(MargaretSqlTable.SQL_PROPERTIES)
        .values("max_possible_homes",
            String.valueOf(maxPossibleHomes))
        .build());
  }

  private void alterHomeList(int addCount) {
    sqlManager.createTableHomesId(maxPossibleHomes);
    List<String> homesId = sqlManager.getTableHomesId();
    String constraint = MargaretSqlTable.PARTNERSHIP_HOMES_LIST.getConstraint();

    int beforeMaxPossibleHomes = maxPossibleHomes - addCount;
    List<String> subList = homesId.subList(
        beforeMaxPossibleHomes, homesId.size());
    List<String> params = new ArrayList<>();
    for (String s : subList) {
      String homeId = s + constraint;
      params.add(homeId);
    }

    statementProcessor.update(dsl
        .alterTable(MargaretSqlTable.PARTNERSHIP_HOMES_LIST)
        .addColumns(params.toArray(ArrayUtils.EMPTY_STRING_ARRAY))
        .build());
    statementProcessor.update(dsl
        .update(MargaretSqlTable.SQL_PROPERTIES)
        .set("value", String.valueOf(maxPossibleHomes))
        .where("id", "max_possible_homes")
        .build());
  }

  public void deleteOutdatedEntries() {
    String table = MargaretSqlTable.PLAYER_DATA.getName();
    Date expire = Date.valueOf(LocalDate.now().minusDays(daysToExpire));
    // No-DSL due to operators limitations
    ResultMap resultMap = processor.queryMap(
        "SELECT id, partnerid FROM " + table + " WHERE last_date <= ?", expire);
    if (!CommonsUtils.isNullOrEmpty(resultMap)) {
      executeBatchPlayers(resultMap);
    }
  }

  private void executeBatchPlayers(ResultMap resultMap) {
    List<String> uuidsOutdated = new ArrayList<>();
    List<String> uuidsPlayerPartner = new ArrayList<>();
    List<StatementBasicData> partnersIdOutdated = new ArrayList<>();
    List<Map<String, SqlObject>> rows = resultMap.getRows();
    for (Map<String, SqlObject> row : rows) {
      String id = row.get("id").getString();
      String partnerid = row.get("partnerid").getString();
      uuidsOutdated.add(id);
      if (!partnerid.equals(EmptyObjects.getEmptyPartnerId())) {
        ResultMap query = statementProcessor.queryMap(dsl
            .select("player1", "player2")
            .from(MargaretSqlTable.PARTNERSHIP_DATA)
            .where("id", partnerid)
            .build());
        if (!CommonsUtils.isNullOrEmpty(query)) {
          String player1 = row.get("player1").getString();
          String player2 = row.get("player2").getString();
          TypePair<String> pair = new TypePairImpl<>(player1, player2);
          int side = pair.getSide(id);
          Preconditions.checkArgument(
              side != 0,
              "Incorrect partner data (outdated data?)");
          uuidsPlayerPartner.add(pair.getBySide(side));
        }
        partnersIdOutdated.add(StatementBasicData
            .wrapParameters(partnerid));
      }
    }

    List<StatementBasicData> uuidsWrapper =
        StatementBasicData
            .wrapSingleParameters(uuidsOutdated);
    deleteOutdatedPlayers(uuidsWrapper,
        uuidsPlayerPartner);
    deleteOutdatedPartners(
        partnersIdOutdated
    );
  }

  private void deleteOutdatedPlayers(List<StatementBasicData> uuidsWrapper,
                                     List<String> uuidsPlayerPartner) {
    if (!uuidsWrapper.isEmpty()) {
      statementProcessor.reuseLargeBatch(dsl
              .deleteFrom(MargaretSqlTable.PLAYER_DATA)
              .where("id", 0)
              .build(),
          uuidsWrapper,
          1);
      statementProcessor.reuseLargeBatch(dsl
              .deleteFrom(MargaretSqlTable.PLAYER_SETTINGS)
              .where("id", 0)
              .build(),
          uuidsWrapper,
          1);
    }
    if (!uuidsPlayerPartner.isEmpty()) {
      statementProcessor.update(dsl
          .update(MargaretSqlTable.PLAYER_DATA)
          .set("partnerid", "--------")
          .where("id", uuidsPlayerPartner)
          .build());
    }
  }

  private void deleteOutdatedPartners(List<StatementBasicData> partnerIdWrapper) {
    if (!partnerIdWrapper.isEmpty()) {
      statementProcessor.reuseLargeBatch(dsl
              .deleteFrom(MargaretSqlTable.PARTNERSHIP_DATA)
              .where("id", 0)
              .build(),
          partnerIdWrapper,
          1);
      statementProcessor.reuseLargeBatch(dsl
              .deleteFrom(MargaretSqlTable.PARTNERSHIP_PROPERTIES)
              .where("id", 0)
              .build(),
          partnerIdWrapper,
          1);
      statementProcessor.reuseLargeBatch(dsl
              .deleteFrom(MargaretSqlTable.PARTNERSHIP_HOME)
              .where("id", 0)
              .build(),
          partnerIdWrapper,
          1);
      statementProcessor.reuseLargeBatch(dsl
              .deleteFrom(MargaretSqlTable.PARTNERSHIP_HOMES_LIST)
              .where("id", 0)
              .build(),
          partnerIdWrapper,
          1);
      statementProcessor.reuseLargeBatch(dsl
              .deleteFrom(MargaretSqlTable.PARTNERSHIP_HOME)
              .where("id", 0)
              .build(),
          partnerIdWrapper,
          1);
    }
  }
}
