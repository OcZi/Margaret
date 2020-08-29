package me.oczi.bukkit.objects;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * A {@link Home} implementation.
 */
public class PartnerHome implements Home {
  private final String id;
  private String alias;

  private final Location location;

  public PartnerHome(String id,
                     String alias,
                     Location location) {
    this.id = id;
    this.alias = alias;
    this.location = location;
  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  @Override
  public void setAlias(String alias) {
    this.alias = alias;
  }

  @Override
  public boolean hasAlias() {
    return !alias.isEmpty();
  }

  @Override
  public Location getLocation() {
    return location;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public String getAlias() {
    return alias;
  }

  @Override
  public void teleport(Player player) {
    player.teleport(location);
  }

  @Override
  public String toString() {
    return "Home{" +
        "id='" + id  +
        ", alias='" + alias +
        ", location=" + location +
        '}';
  }
}
