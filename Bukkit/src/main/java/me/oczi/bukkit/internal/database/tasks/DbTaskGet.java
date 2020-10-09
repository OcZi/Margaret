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
  
  ResultMap getTopOfPartners(int limit);

  int getCountOfPartners();

  Map<String, SqlObject> getPartnerData(String id);

  ResultMap getAnythingOfPartnerData();

  Map<String, SqlObject> getPartnerHomeList(String id);

  Map<String, SqlObject> getHome(String id);

  Map<String, SqlObject> getPartnerProperties(String id);

}
