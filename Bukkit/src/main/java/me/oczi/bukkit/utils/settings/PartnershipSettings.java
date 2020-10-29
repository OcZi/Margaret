package me.oczi.bukkit.utils.settings;

import me.oczi.bukkit.utils.PartnershipPermission;

import static me.oczi.bukkit.utils.PartnershipPermission.*;

// Settings only for player with partner.
public enum PartnershipSettings implements EnumSettings {

  ALLOW_PVP("allow-pvp", true, PVP),
  ALLOW_TELEPORT("allow-teleport", true, TP),
  ALLOW_GIFT("allow-gift", true, GIFT),
  ALLOW_MOUNT("allow-mount", true, MOUNT);

  private final String formalName;
  private final boolean defaultValue;
  private final PartnershipPermission permissionEquivalent;

  PartnershipSettings(String formalName, boolean defaultValue,
                      PartnershipPermission permissionEquivalent) {
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
  public PartnershipPermission getPermissionEquivalent() {
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
