package me.oczi.bukkit.internal.database.tasks;

import java.util.UUID;

public interface DbTaskUpdate {

  void updatePlayerExpire(UUID uuid, int i);

  void updatePlayerData(String columnName,
                        Object param,
                        Object id);

  void updateDoublePlayerData(String columnName,
                              Object param,
                              Object id1,
                              Object id2);

  void updatePlayerSetting(String settingName,
                           Object param,
                           Object id);

  void updatePartnerData(String columnName,
                         Object param,
                         String id);

  void updatePartnerProperty(String settingName,
                             Object param,
                             String id);
}
