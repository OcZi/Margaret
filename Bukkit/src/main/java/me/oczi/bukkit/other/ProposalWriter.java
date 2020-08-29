package me.oczi.bukkit.other;

import com.github.benmanes.caffeine.cache.CacheWriter;
import com.github.benmanes.caffeine.cache.RemovalCause;
import me.oczi.bukkit.objects.collections.CacheSet;
import me.oczi.bukkit.objects.Proposal;
import me.oczi.bukkit.objects.player.MargaretPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * {@link CacheWriter} for Proposal {@link CacheSet}.
 * Used as a RemovalListener synchronized.
 */
public class ProposalWriter
    implements CacheWriter<Proposal, Long> {

  @Override
  public void write(@NonNull Proposal key, @NonNull Long value) {}

  @Override
  public void delete(@NonNull Proposal proposal,
                     @Nullable Long value,
                     @NonNull RemovalCause cause) {
    if (!proposal.isEmpty()) {
      MargaretPlayer margaretPlayer = proposal.getSender();
      if (!margaretPlayer.isEmpty()) {
        margaretPlayer.clearCurrentProposal();
      }
    }
  }
}
