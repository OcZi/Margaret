package me.oczi.bukkit.objects.collections;

import me.oczi.bukkit.objects.player.PlayerDataPair;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;

public interface PartnerTop extends Set<PlayerDataPair> {

  PlayerDataPair get(int index);

  ConcurrentMap<Integer, PlayerDataPair> asMap();
}
