package me.oczi.bukkit.objects.partner;

import me.oczi.bukkit.objects.collections.HomeList;
import me.oczi.bukkit.objects.collections.PartnerPermissionSet;
import me.oczi.bukkit.utils.PartnerPermission;

import java.util.UUID;

public class PartnerImpl implements Partner{
  private final String id;

  private final UUID uuid1;
  private final UUID uuid2;

  private final PartnerPermissionSet permissions;
  private final HomeList homes;

  private String relation;

  public PartnerImpl(PartnerData partnerData,
                     PartnerProperties properties) {
    this.id = partnerData.getId();
    this.uuid1 = partnerData.getPlayerUUID1();
    this.uuid2 = partnerData.getPlayerUUID2();
    this.relation = partnerData.getRelation();
    this.permissions = properties.getPartnerPermissionSet();
    this.homes = properties.getHomeList();
  }

  @Override
  public void setRelation(String relation) {
    this.relation = relation;
  }

  @Override
  public boolean hasPermission(PartnerPermission perm) {
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
  public PartnerPermissionSet getPermissions() {
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
