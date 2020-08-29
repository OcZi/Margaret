package me.oczi.bukkit.utils.settings;

import me.oczi.bukkit.utils.PartnerPermission;

public enum CacheSettings implements EnumSettings {

  CHAT("chat", false, PartnerPermission.CHAT),
  CHAT_SPY("chat-spy", false);

  private final String formalName;
  private final boolean defaultValue;
  private final PartnerPermission permissionEquivalent;

  CacheSettings(String formalName, boolean defaultValue) {
    this.formalName = formalName;
    this.defaultValue = defaultValue;
    this.permissionEquivalent = null;
  }

  CacheSettings(String formalName, boolean defaultValue,
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
    return formalName.isEmpty() ? getName() : formalName;
  }
}
