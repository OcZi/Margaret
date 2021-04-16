package me.oczi.bukkit.utils.settings;

import me.oczi.bukkit.utils.PartnershipPermission;

public enum CacheSetting implements EnumSetting {

  CHAT("chat", false, PartnershipPermission.CHAT),
  CHAT_SPY("chat-spy", false);

  private final String formalName;
  private final boolean defaultValue;
  private final PartnershipPermission permissionEquivalent;

  CacheSetting(String formalName, boolean defaultValue) {
    this.formalName = formalName;
    this.defaultValue = defaultValue;
    this.permissionEquivalent = null;
  }

  CacheSetting(String formalName, boolean defaultValue,
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
