package me.oczi.bukkit.internal;

import me.oczi.bukkit.internal.objectcycle.partner.PartnerObjectLoader;
import me.oczi.bukkit.internal.objectcycle.player.PlayerObjectLoader;

public class ObjectCycleManagerImpl implements ObjectCycleManager {
  private final PlayerObjectLoader playerLoader;
  private final PartnerObjectLoader partnerLoader;

  public ObjectCycleManagerImpl(MemoryManager memoryManager) {
    this.playerLoader = new PlayerObjectLoader(memoryManager);
    this.partnerLoader = new PartnerObjectLoader(memoryManager);
  }

  @Override
  public PartnerObjectLoader getPartnerLoader() {
    return partnerLoader;
  }

  @Override
  public PlayerObjectLoader getPlayerLoader() {
    return playerLoader;
  }
}
