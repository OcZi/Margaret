package me.oczi.bukkit.utils;

import net.kyori.text.format.TextColor;
import org.bukkit.ChatColor;

public enum MargaretColor {

  BASE(TextColor.GRAY),
  RELEVANT(TextColor.DARK_AQUA),
  IRRELEVANT(TextColor.DARK_GRAY),
  IMPORTANT(TextColor.BLUE),
  WARNING(TextColor.RED);

  private final TextColor textColor;

  MargaretColor(TextColor textColor) {
    this.textColor = textColor;
  }

  public TextColor getDefaultTextColor() { return textColor; }

  public ChatColor getChatColor() {
    return ChatColor.valueOf(textColor.toString().toUpperCase());
  }
}
