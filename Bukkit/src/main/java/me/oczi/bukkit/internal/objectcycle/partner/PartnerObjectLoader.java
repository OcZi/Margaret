package me.oczi.bukkit.internal.objectcycle.partner;

import me.oczi.bukkit.internal.MemoryManager;
import me.oczi.bukkit.internal.objectcycle.AbstractObjectLoader;
import me.oczi.bukkit.objects.partnership.Partnership;
import me.oczi.bukkit.objects.partnership.PartnershipData;
import me.oczi.bukkit.objects.partnership.PartnershipImpl;
import me.oczi.bukkit.objects.partnership.PartnershipProperties;
import me.oczi.bukkit.objects.player.MargaretPlayer;
import me.oczi.bukkit.utils.MessageUtils;

public class PartnerObjectLoader extends AbstractObjectLoader<String> {
  private final PartnerObjectBuilder builder;

  public PartnerObjectLoader(MemoryManager memoryManager) {
    super(memoryManager);
    this.builder = new PartnerObjectBuilder(core.getDatabaseTask());
  }

  @Override
  public void load(String id) {
    // Case if partner is loaded by the other partner.
    if (persistenceCache.containsPartner(id)) {
      return;
    }

    if (garbageCache != null &&
        garbageCache.containsPartner(id)) {
      swapObject(id);
      return;
    }

    PartnershipData partnershipData = builder.initPartnerData(id);
    if (partnershipData == null) {
      MessageUtils.warning("Partner " + id + " data is null.");
      return;
    }

    PartnershipProperties partnershipProperties = builder.initPartnerProperties(id);
    Partnership partnership = new PartnershipImpl(partnershipData, partnershipProperties);
    persistenceCache.putPartner(partnership.getId(), partnership);
  }

  public void newPartner(MargaretPlayer margaretPlayer1,
                         MargaretPlayer margaretPlayer2) {
    Partnership partnership = builder.newPartner(
        margaretPlayer1,
        margaretPlayer2);
    if (partnership != null) {
      persistenceCache.putPartner(partnership.getId(), partnership);
    }
  }

  @Override
  public void close(String id) {
    if (cache.isGarbageCache()) {
      Partnership partnership = persistenceCache.getPartner(id);
      garbageCache.putPartner(id, partnership);
      MessageUtils.debug(id + " Partner swap from Persistence to Garbage");
    }

    persistenceCache.removePartner(id);
    MessageUtils.debug(id + " session ended");
  }

  @Override
  public void swapObject(String id) {
    Partnership partnership = garbageCache.getPartner(id);
    persistenceCache.putPartner(id, partnership);
    garbageCache.removePartner(id);
    MessageUtils.debug(id + " Partner swap from Garbage to Persistence");
  }
}
