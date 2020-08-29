package me.oczi.bukkit.internal.database.tasks;

import me.oczi.common.storage.sql.orm.result.ResultMap;
import me.oczi.common.storage.sql.orm.result.SqlObject;

import java.util.Date;
import java.util.UUID;

public interface DbTaskGet {

  SqlObject getColumnPlayerData(UUID uuid, String columnName);

  ResultMap getPlayerData(UUID uuid);

  Date getPlayerExpire(UUID uuid);

  ResultMap getPlayerSettings(UUID uuid);

  boolean getPlayerSetting(UUID uuid,
                           String settingName);

  ResultMap getPartnerData(String id);

  ResultMap getPartnerHomeList(String id);

  ResultMap getHome(String id);

  ResultMap getPartnerProperties(String id);

}
