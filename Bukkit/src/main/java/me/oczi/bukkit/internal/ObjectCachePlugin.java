package me.oczi.bukkit.internal;

import me.oczi.bukkit.objects.partner.Partner;
import me.oczi.bukkit.objects.player.MargaretPlayer;
import me.oczi.common.api.memory.ObjectCache;

/**
 * A Object manager for {@link MargaretPlayer} and {@link Partner}.
 */
public interface ObjectCachePlugin
    extends ObjectCache<MargaretPlayer, Partner> {
}
