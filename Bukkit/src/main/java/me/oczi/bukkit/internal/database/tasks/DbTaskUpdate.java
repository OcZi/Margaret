package me.oczi.bukkit.internal.database.tasks;

import me.oczi.bukkit.objects.Home;
import org.bukkit.Location;

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

  void updatePartnershipData(String columnName,
                             Object param,
                             String id);

  void updatePartnershipProperty(String settingName,
                                 Object param,
                                 String id);

  void updateHomeData(String columnName,
                      String param,
                      String homeId);

  void updateHomeLocation(Location location,
                          String partnerId,
                          Home home);
}
