package me.oczi.bukkit.objects;

import me.oczi.bukkit.objects.collections.CacheSet;
import me.oczi.bukkit.objects.collections.CacheSetImpl;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Cooldown player class.
 */
public class CooldownPlayerImpl implements CooldownPlayer {
  private final int cooldownEviction;
  private final CacheSet<UUID> cacheSet;

  public CooldownPlayerImpl(int cooldown) {
    this.cooldownEviction = cooldown;
    this.cacheSet = new CacheSetImpl<>(cooldownEviction);
  }

  @Override
  public boolean hasCooldown(Player player) {
    return hasCooldown(player.getUniqueId());
  }

  @Override
  public boolean hasCooldown(UUID uuid) {
    return cacheSet.containsAsMap(uuid);
  }

  @Override
  public void setCooldown(Player player) {
    setCooldown(player.getUniqueId());
  }

  @Override
  public void setCooldown(UUID uuid) {
    if (!cacheSet.containsAsMap(uuid)) {
      cacheSet.add(uuid);
    }
  }

  @Override
  public double getCountdownInSeconds(Player player) {
    return getCountdownInSeconds(player.getUniqueId());
  }

  @Override
  public double getCountdownInSeconds(UUID uuid) {
    long expire = cacheSet.getExpireOf(uuid);
    return (expire - System.currentTimeMillis()) / 1000.0;
  }

  @Override
  public int getCooldownEviction() {
    return cooldownEviction;
  }

  @Override
  public CacheSet<UUID> getCacheSet() {
    return cacheSet;
  }
}

