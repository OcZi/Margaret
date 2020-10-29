package me.oczi.bukkit.objects.partnership;

import me.oczi.bukkit.objects.collections.HomeList;
import me.oczi.bukkit.objects.collections.PartnershipPermissionSet;
import me.oczi.bukkit.utils.PartnershipPermission;

import java.util.UUID;

public class PartnershipImpl implements Partnership {
  private final String id;

  private final UUID uuid1;
  private final UUID uuid2;

  private final PartnershipPermissionSet permissions;
  private final HomeList homes;

  private String relation;

  public PartnershipImpl(PartnershipData partnershipData,
                         PartnershipProperties properties) {
    this.id = partnershipData.getId();
    this.uuid1 = partnershipData.getPlayerUUID1();
    this.uuid2 = partnershipData.getPlayerUUID2();
    this.relation = partnershipData.getRelation();
    this.permissions = properties.getPartnershipPermissionSet();
    this.homes = properties.getHomeList();
  }

  @Override
  public void setRelation(String relation) {
    this.relation = relation;
  }

  @Override
  public boolean hasPermission(PartnershipPermission perm) {
    return permissions.getPermissions().contains(perm);
  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public UUID getUuid1() {
    return uuid1;
  }

  @Override
  public UUID getUuid2() {
    return uuid2;
  }

  @Override
  public PartnershipPermissionSet getPermissions() {
    return permissions;
  }

  @Override
  public HomeList getHomeList() {
    return homes;
  }

  @Override
  public String getRelation() {
    return relation;
  }

  @Override
  public String toString() {
    return "PartnerImpl{" +
        "id='" + id + '\'' +
        ", uuid1=" + uuid1 +
        ", uuid2=" + uuid2 +
        ", permissions=" + permissions +
        ", homes=" + homes +
        ", relation='" + relation + '\'' +
        '}';
  }
}
