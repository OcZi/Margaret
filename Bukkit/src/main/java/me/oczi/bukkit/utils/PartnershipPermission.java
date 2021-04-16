package me.oczi.bukkit.utils;

import me.oczi.common.utils.BitMasks;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * All the permissions of Partnership.
 */
public enum PartnershipPermission {

  PVP("margaret.partnership.pvp"),
  TP("margaret.partnership.tp"),
  HOME("margaret.partnership.home"),
  GIFT("margaret.partnership.gift"),
  HEAL("margaret.partnership.heal"),
  MOUNT("margaret.partnership.mount"),
  CHAT("margaret.partnership.chat"),
  RELATION("margaret.partnership.relation"),
  CUSTOM_RELATION("margaret.partnership.custom-relation");

  private final String node;

  PartnershipPermission(String node) {
    this.node = node;
  }

  private static final Map<String, PartnershipPermission> formattedPermissions =
      createFormattedPermissionMap();

  private static Map<String, PartnershipPermission> createFormattedPermissionMap() {
    Map<String, PartnershipPermission> map = new HashMap<>();
    for (PartnershipPermission value : values()) {
      map.put(value.toString()
          .toLowerCase()
          .replace("_", "-"),
          value);
    }
    return map;
  }

  private static final Map<String, PartnershipPermission> permissions =
      createPermissionMap();

  private static Map<String, PartnershipPermission> createPermissionMap() {
    Map<String, PartnershipPermission> map = new HashMap<>();
    for (PartnershipPermission entry : values()) {
      map.putIfAbsent(entry.getNode(), entry);
    }

    return map;
  }

  public static Map<String, PartnershipPermission> getFormattedPermissions() {
    return formattedPermissions;
  }

  public static Map<String, PartnershipPermission> getPermissions() {
    return permissions;
  }

  public String getNode() {
    return node;
  }

  public static Set<Integer> getPermissionsAsBits() {
    Set<Integer> bits = new HashSet<>();
    for (PartnershipPermission perm : permissions.values())
      bits.add(BitMasks.mask(perm));
    return bits;
  }
}
