package me.oczi.bukkit.internal.database.tasks;

import java.util.List;

public interface DbTaskSet {

  void setHomeList(String id, List<Object> homeList);
}
