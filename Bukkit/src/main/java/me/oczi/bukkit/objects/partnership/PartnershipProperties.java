package me.oczi.bukkit.objects.partnership;

import me.oczi.bukkit.objects.collections.HomeList;
import me.oczi.bukkit.objects.collections.PartnershipPermissionSet;
import org.jetbrains.annotations.Nullable;


public class PartnershipProperties {
  private final PartnershipPermissionSet partnershipPermissionSet;
  private final HomeList homeList;

  public PartnershipProperties(PartnershipPermissionSet partnershipPermissionSet,
                               @Nullable HomeList homeList) {
    this.partnershipPermissionSet = partnershipPermissionSet;
    this.homeList = homeList;
  }

  public int getBitsOfPermissions() {
    return partnershipPermissionSet.getBits();
  }

  public PartnershipPermissionSet getPartnershipPermissionSet() {
    return partnershipPermissionSet;
  }

  public boolean hasHomes() {
    return homeList != null;
  }

  public HomeList getHomeList() {
    return homeList;
  }
}
