package me.oczi.bukkit.utils;

import org.bukkit.ChatColor;

public enum DefaultGender {

  UNKNOWN("unknown", ChatColor.GRAY, "?");

  final String name;
  final ChatColor color;
  final String prefix;

  DefaultGender(String name, ChatColor color, String prefix) {
    this.name = name;
    this.color = color;
    this.prefix = prefix;
  }

  public String getPrefix() {
    return prefix;
  }

  public ChatColor getColor() {
    return color;
  }

  public String getName() {
    return name.toLowerCase();
  }
}
