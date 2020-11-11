package me.oczi.bukkit.objects;

import org.bukkit.ChatColor;

/**
 * POJO Gender class.
 */
public class GenderImpl implements Gender{

  private final String realName;
  private final String formalName;
  private final ChatColor color;
  private final String prefix;

  public GenderImpl(String realName,
                    String formalName,
                    ChatColor color,
                    String prefix) {
    this.realName = realName;
    this.formalName = formalName;
    this.color = color;
    this.prefix = prefix;
  }

  @Override
  public String getFormalName() {
    return formalName;
  }

  @Override
  public String getRealName() {
    return realName;
  }

  @Override
  public ChatColor getChatColor() {
    return color;
  }

  @Override
  public String getPrefix() {
    return prefix;
  }

  @Override
  public String getFormalNameColorized() {
    return color + formalName;
  }

  @Override
  public String getPrefixColorized() {
    return color + prefix;
  }
}
