package me.oczi.bukkit.utils.settings;

import me.oczi.bukkit.utils.PartnerPermission;

import static me.oczi.bukkit.utils.PartnerPermission.*;

// Settings only for player with partner.
public enum PartnerSettings implements EnumSettings {

  ALLOW_PVP("allow-pvp", true, PVP),
  ALLOW_TELEPORT("allow-teleport", true, TP),
  ALLOW_GIFT("allow-gift", true, GIFT),
  ALLOW_MOUNT("allow-mount", true, MOUNT);

  private final String formalName;
  private final boolean defaultValue;
  private final PartnerPermission permissionEquivalent;

  PartnerSettings(String formalName, boolean defaultValue,
                  PartnerPermission permissionEquivalent) {
    this.formalName = formalName;
    this.defaultValue = defaultValue;
    this.permissionEquivalent = permissionEquivalent;
  }

  @Override
  public boolean havePermissionEquivalent() {
    return permissionEquivalent != null;
  }

  @Override
  public boolean getDefaultValue() {
    return defaultValue;
  }

  @Override
  public PartnerPermission getPermissionEquivalent() {
    return permissionEquivalent;
  }

  @Override
  public String getName() {
    return toString().toLowerCase();
  }

  @Override
  public String getFormalName() {
    return formalName;
  }
}
