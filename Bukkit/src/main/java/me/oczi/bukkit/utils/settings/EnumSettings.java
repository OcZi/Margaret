package me.oczi.bukkit.utils.settings;

import me.oczi.bukkit.utils.PartnershipPermission;

public interface EnumSettings {

  String getName();

  String getFormalName();

  boolean havePermissionEquivalent();

  boolean getDefaultValue();

  PartnershipPermission getPermissionEquivalent();
}
