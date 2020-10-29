package me.oczi.bukkit.other;

import com.github.benmanes.caffeine.cache.CacheWriter;
import com.github.benmanes.caffeine.cache.RemovalCause;
import me.oczi.bukkit.objects.collections.PartnershipTop;
import me.oczi.bukkit.objects.player.PlayerDataPair;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class PartnershipTopWriter implements CacheWriter<Integer, PlayerDataPair> {
  private final PartnershipTop top;

  public PartnershipTopWriter(PartnershipTop top) {
    this.top = top;
  }

  @Override
  public void write(@NonNull Integer key, @NonNull PlayerDataPair value) {}

  @Override
  public void delete(@NonNull Integer key, @Nullable PlayerDataPair value, @NonNull RemovalCause cause) {
    top.clearPages();
  }
}
