package me.oczi.bukkit.internal.objectcycle.partner;

import me.oczi.bukkit.internal.MemoryManager;
import me.oczi.bukkit.internal.objectcycle.AbstractObjectLoader;
import me.oczi.bukkit.objects.partner.Partner;
import me.oczi.bukkit.objects.partner.PartnerData;
import me.oczi.bukkit.objects.partner.PartnerImpl;
import me.oczi.bukkit.objects.partner.PartnerProperties;
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

    PartnerData partnerData = builder.initPartnerData(id);
    if (partnerData == null) {
      MessageUtils.warning("Partner " + id + " data is null.");
      return;
    }

    PartnerProperties partnerProperties = builder.initPartnerProperties(id);
    Partner partner = new PartnerImpl(partnerData, partnerProperties);
    persistenceCache.putPartner(partner.getId(), partner);
  }

  public void newPartner(MargaretPlayer margaretPlayer1,
                         MargaretPlayer margaretPlayer2) {
    Partner partner = builder.newPartner(
        margaretPlayer1,
        margaretPlayer2);
    if (partner != null) {
      persistenceCache.putPartner(partner.getId(), partner);
    }
  }

  @Override
  public void close(String id) {
    if (cache.isGarbageCache()) {
      Partner partner = persistenceCache.getPartner(id);
      garbageCache.putPartner(id, partner);
      MessageUtils.debug(id + " Partner swap from Persistence to Garbage");
    }

    persistenceCache.removePartner(id);
    MessageUtils.debug(id + " session ended");
  }

  @Override
  public void swapObject(String id) {
    Partner partner = garbageCache.getPartner(id);
    persistenceCache.putPartner(id, partner);
    garbageCache.removePartner(id);
    MessageUtils.debug(id + " Partner swap from Garbage to Persistence");
  }
}
