package me.oczi.bukkit.internal.database.sql;

import me.oczi.bukkit.internal.database.DatabaseManager;

public interface SqlManager extends DatabaseManager {

  void createTableHomesId();

  void createTableHomesId(int size);
}
