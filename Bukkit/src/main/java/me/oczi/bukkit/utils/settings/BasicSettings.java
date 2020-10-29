package me.oczi.bukkit.utils.settings;

import me.oczi.bukkit.utils.PartnershipPermission;

import java.util.HashSet;
import java.util.Set;

public enum BasicSettings implements EnumSettings {

  SOUND_EFFECTS("sound-effects", true),
  SHOW_GENDER("show-gender", true),
  SHOW_PARTNER("show-partner", true),
  ALLOW_PROPOSALS("allow-proposals", true);

  private final String formalName;
  private final boolean defaultValue;

  BasicSettings(String formalName, boolean defaultValue) {
    this.formalName = formalName;
    this.defaultValue = defaultValue;
  }

  @Override
  public boolean havePermissionEquivalent() {
    return false;
  }

  @Override
  public boolean getDefaultValue() {
    return defaultValue;
  }

  @Override
  public PartnershipPermission getPermissionEquivalent() {
    return null;
  }

  @Deprecated
  public static Set<String> getValuesNames() {
    Set<String> set = new HashSet<>();
    for (BasicSettings value : values()) {
      set.add(value.name().toLowerCase());
    }

    return set;
  }

  public static int length() {
    return values().length;
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
