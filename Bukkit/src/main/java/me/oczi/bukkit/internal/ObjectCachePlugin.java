package me.oczi.bukkit.internal;

import me.oczi.bukkit.objects.partnership.Partnership;
import me.oczi.bukkit.objects.player.MargaretPlayer;
import me.oczi.common.api.memory.ObjectCache;

/**
 * A Object manager for {@link MargaretPlayer} and {@link Partnership}.
 */
public interface ObjectCachePlugin
    extends ObjectCache<MargaretPlayer, Partnership> {
}
