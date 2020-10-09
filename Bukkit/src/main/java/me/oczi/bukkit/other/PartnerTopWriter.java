package me.oczi.bukkit.other;

import com.github.benmanes.caffeine.cache.CacheWriter;
import com.github.benmanes.caffeine.cache.RemovalCause;
import me.oczi.bukkit.objects.collections.PartnerTop;
import me.oczi.bukkit.objects.player.PlayerDataPair;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class PartnerTopWriter implements CacheWriter<Integer, PlayerDataPair> {
  private final PartnerTop top;

  public PartnerTopWriter(PartnerTop top) {
    this.top = top;
  }

  @Override
  public void write(@NonNull Integer key, @NonNull PlayerDataPair value) {}

  @Override
  public void delete(@NonNull Integer key, @Nullable PlayerDataPair value, @NonNull RemovalCause cause) {
    top.clearPages();
  }
}
