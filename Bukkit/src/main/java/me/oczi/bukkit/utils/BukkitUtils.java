package me.oczi.bukkit.utils;

import net.kyori.text.format.TextColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Utils related to Bukkit.
 */
public interface BukkitUtils {

  /**
   * Get name of item formatted.
   *
   * @param itemStack ItemStack to get name.
   * @return Name of item formatted.
   */
  static String getNameOfItem(ItemStack itemStack) {
    ItemMeta itemMeta = itemStack.getItemMeta();
    String itemName = itemMeta.hasDisplayName()
        ? itemMeta.getDisplayName()
        : itemStack.getType().name();
    itemName = itemName.toLowerCase();
    return StringUtils.capitalize(
        itemName.replace("_", " "));
  }

  /**
   * Call event and get result of them.
   *
   * @param event Event to call.
   * @return Event is cancelled or not.
   */
  static boolean callEventAndGetResult(Event event) {
    Bukkit.getPluginManager().callEvent(event);
    return event instanceof Cancellable &&
        ((Cancellable) event).isCancelled();
  }

  /**
   * Retrieve relevant information of Location
   * into a String.
   *
   * @param location Location to String.
   * @return Relevant information of Location.
   */
  static String locationToString(Location location) {
    return String.format(
        "x: %s, y: %s, z: %s",
        location.getX(),
        location.getY(),
        location.getZ());
  }

  /**
   * Check Item is valid.
   *
   * @param itemStack Item to check.
   * @return is valid or not.
   */
  static boolean isValidItem(ItemStack itemStack) {
    return itemStack != null &&
        itemStack.getType() != Material.AIR;
  }

  /**
   * Check Inventory's player is full.
   *
   * @param player Player to check.
   * @return is full or not.
   */
  static boolean isInventoryFull(Player player) {
    return player.getInventory()
        .firstEmpty() == -1;
  }

  /**
   * Check is a valid ChatColor with TextColor's name.
   *
   * @param color Color name to check.
   * @return is valid or not.
   */
  static boolean isChatColor(String color) {
    return TextColor.NAMES.get(color).isPresent();
  }
}
