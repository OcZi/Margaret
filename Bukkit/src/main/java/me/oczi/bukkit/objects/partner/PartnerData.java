package me.oczi.bukkit.objects.partner;

import java.util.UUID;

public class PartnerData {

  private final String id;
  private final UUID uuid1;
  private final UUID uuid2;
  private final String relation;

  public PartnerData(String id,
                     UUID uuid1,
                     UUID uuid2,
                     String relation) {
    this.id = id;
    this.uuid1 = uuid1;
    this.uuid2 = uuid2;
    this.relation = relation;
  }

  public String getId() {
    return id;
  }

  public UUID getPlayerUUID1() {
    return uuid1;
  }

  public UUID getPlayerUUID2() {
    return uuid2;
  }

  public String getRelation() {
    return relation;
  }
}
