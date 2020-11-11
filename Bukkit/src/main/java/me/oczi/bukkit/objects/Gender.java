package me.oczi.bukkit.objects;

import org.bukkit.ChatColor;

/**
 * A Gender in Minecraft.
 */
public interface Gender {

  /**
   * Get the formal name of Gender.
   * @return Formal name.
   */
  String getFormalName();

  /**
   * Get the real name of gender.
   * @return Real name.
   */
  String getRealName();

  /**
   * Get the {@link ChatColor} of gender.
   * @return ChatColor.
   */
  ChatColor getChatColor();

  /**
   * Get the prefix of gender.
   * @return Prefix.
   */
  String getPrefix();

  /**
   * Get the formal name colorized with
   * their ChatColor. of Gender.
   * @return Formal name colorized.
   */
  String getFormalNameColorized();


  /**
   * Get the prefix colorized with
   * their ChatColor. of Gender.
   * @return Prefix colorized.
   */
  String getPrefixColorized();
}
