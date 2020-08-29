package me.oczi.bukkit.utils.settings;

import me.oczi.bukkit.utils.PartnerPermission;

public interface EnumSettings {

  String getName();

  String getFormalName();

  boolean havePermissionEquivalent();

  boolean getDefaultValue();

  PartnerPermission getPermissionEquivalent();
}
