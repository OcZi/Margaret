package me.oczi.bukkit.objects.partner;

import me.oczi.bukkit.objects.collections.HomeList;
import me.oczi.bukkit.objects.collections.PartnerPermissionSet;
import org.jetbrains.annotations.Nullable;


public class PartnerProperties {
  private final PartnerPermissionSet partnerPermissionSet;
  private final HomeList homeList;

  public PartnerProperties(PartnerPermissionSet partnerPermissionSet,
                           @Nullable HomeList homeList) {
    this.partnerPermissionSet = partnerPermissionSet;
    this.homeList = homeList;
  }

  public int getBitsOfPermissions() {
    return partnerPermissionSet.getBits();
  }

  public PartnerPermissionSet getPartnerPermissionSet() {
    return partnerPermissionSet;
  }

  public boolean hasHomes() {
    return homeList != null;
  }

  public HomeList getHomeList() {
    return homeList;
  }
}
