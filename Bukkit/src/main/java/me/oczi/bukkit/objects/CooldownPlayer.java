package me.oczi.bukkit.objects;

import me.oczi.bukkit.objects.collections.CacheSet;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Cooldown for players with a internal {@link CacheSet}.
 */
public interface CooldownPlayer {

  boolean hasCooldown(Player player);

  boolean hasCooldown(UUID uuid);

  void setCooldown(Player player);

  void setCooldown(UUID uuid);

  double getCountdownInSeconds(Player player);

  double getCountdownInSeconds(UUID uuid);

  int getCooldownEviction();

  CacheSet<UUID> getCacheSet();
}
