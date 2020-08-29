package me.oczi.bukkit.utils;

import me.oczi.common.utils.BitMasks;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * All the permissions of partner.
 */
public enum PartnerPermission {

  PVP("margaret.partner.pvp"),
  TP("margaret.partner.tp"),
  HOME("margaret.partner.home"),
  GIFT("margaret.partner.gift"),
  HEAL("margaret.partner.heal"),
  MOUNT("margaret.partner.mount"),
  CHAT("margaret.partner.chat"),
  RELATION("margaret.partner.relation"),
  CUSTOM_RELATION("margaret.partner.custom-relation");

  private final String node;

  PartnerPermission(String node) {
    this.node = node;
  }

  private static final Map<String, PartnerPermission> permissions =
      createPermissionMap();

  private static final Set<String> permissionNodes = permissions.keySet();

  private static Map<String, PartnerPermission> createPermissionMap() {
    Map<String, PartnerPermission> map = new HashMap<>();
    for (PartnerPermission entry : PartnerPermission.values()) {
      map.putIfAbsent(entry.getNode(), entry);
    }

    return map;
  }

  public static Map<String, PartnerPermission> getPermissions() {
    return permissions;
  }

  public static Set<String> getPermissionNodes() {
    return permissionNodes;
  }

  public String getNode() {
    return node;
  }

  public static Set<Integer> getPermissionsAsBits() {
    Set<Integer> bits = new HashSet<>();
    for (PartnerPermission perm : PartnerPermission.values())
      bits.add(BitMasks.mask(perm));
    return bits;
  }
}
