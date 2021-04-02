package me.oczi.bukkit.internal.objectcycle;

import me.oczi.bukkit.internal.objectcycle.partner.PartnerObjectLoader;
import me.oczi.bukkit.internal.objectcycle.player.PlayerObjectLoader;

public interface ObjectCycleManager {
  PartnerObjectLoader getPartnerLoader();

  PlayerObjectLoader getPlayerLoader();
}
