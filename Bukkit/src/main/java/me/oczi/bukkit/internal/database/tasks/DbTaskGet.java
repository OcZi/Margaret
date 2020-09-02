package me.oczi.bukkit.internal.database.tasks;

import me.oczi.common.storage.sql.dsl.result.ResultMap;
import me.oczi.common.storage.sql.dsl.result.SqlObject;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface DbTaskGet {

  SqlObject getColumnPlayerData(UUID uuid, String columnName);

  ResultMap getPlayerData(UUID uuid);

  ResultMap getAllPlayerData(String... uuids);

  ResultMap getAllPlayerData(List<String> uuids);

  Date getPlayerExpire(UUID uuid);

  ResultMap getPlayerSettings(UUID uuid);

  boolean getPlayerSetting(UUID uuid,
                           String settingName);
  
  ResultMap getTopOfPartners(int limit);

  int getCountOfPartners();

  ResultMap getPartnerData(String id);

  ResultMap getPartnerHomeList(String id);

  ResultMap getHome(String id);

  ResultMap getPartnerProperties(String id);

}
