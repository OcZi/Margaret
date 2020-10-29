package me.oczi.bukkit.objects.collections;

import me.oczi.bukkit.objects.player.PlayerDataPair;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

public interface PartnershipTop extends Set<PlayerDataPair> {

  PlayerDataPair get(int index);

  List<PlayerDataPair> getPage(int numPage);

  List<List<PlayerDataPair>> getPages();

  int getEntryStartedOfPage(int numPage);

  int getMaxEntries();

  int getEntriesPerPage();

  void clearPages();

  ConcurrentMap<Integer, PlayerDataPair> asMap();
}
