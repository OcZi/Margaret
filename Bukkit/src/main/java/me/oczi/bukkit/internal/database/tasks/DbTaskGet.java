package me.oczi.bukkit.internal.database.tasks;

import me.oczi.common.storage.sql.dsl.result.ResultMap;
import me.oczi.common.storage.sql.dsl.result.SqlObject;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface DbTaskGet {

  SqlObject getColumnPlayerData(UUID uuid, String columnName);

  Map<String, SqlObject> getPlayerData(UUID uuid);

  ResultMap getAllPlayerData(String... uuids);

  ResultMap getAllPlayerData(List<String> uuids);

  Date getPlayerExpire(UUID uuid);

  Map<String, SqlObject> getPlayerSettings(UUID uuid);

  boolean getPlayerSetting(UUID uuid,
                           String settingName);
  
  ResultMap getTopOfPartnerships(int limit);

  int getCountOfPartnerships();

  Map<String, SqlObject> getPartnershipData(String id);

  ResultMap getAnythingOfPartnershipData();

  Map<String, SqlObject> getPartnershipHomeList(String id);

  Map<String, SqlObject> getHome(String id);

  Map<String, SqlObject> getPartnershipProperties(String id);
}
