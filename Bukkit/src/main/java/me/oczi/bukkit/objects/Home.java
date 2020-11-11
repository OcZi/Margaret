package me.oczi.bukkit.objects;

import me.oczi.common.api.Emptyble;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Date;

/**
 * A Home that can be used as a teleport point.
 */
public interface Home extends Emptyble {

  /**
   * Teleport player to this home.
   * @param player Player to teleport.
   */
  void teleport(Player player);

  /**
   * Set alias of home.
   * @param alias Alias to set.
   */
  void setAlias(String alias);

  void setLocation(Location location);

  /**
   * Check if home has a alias.
   */
  boolean hasAlias();

  /**
   * Get ID of home.
   * @return Home's ID.
   */
  String getId();

  /**
   * Get Alias of home.
   * @return Home's alias.
   */
  String getAlias();

  /**
   * Get location of home
   * @return Home's location.
   */
  Location getLocation();

  /**
   * Get creation date of home.
   * @return Home creation date.
   */
  Date getCreationDate();
}
