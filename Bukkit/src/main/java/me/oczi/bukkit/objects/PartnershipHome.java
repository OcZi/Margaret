package me.oczi.bukkit.objects;

import com.google.common.base.Objects;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Date;

/**
 * A {@link Home} implementation.
 */
public class PartnershipHome implements Home {
  private final String id;
  private final Date creationDate;
  private String alias;

  private Location location;

  public PartnershipHome(String id,
                         Date creationDate,
                         String alias,
                         Location location) {
    this.id = id;
    this.creationDate = creationDate;
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
  public void setLocation(Location location) {
    this.location = location;
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
  public Date getCreationDate() {
    return creationDate;
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
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PartnershipHome that = (PartnershipHome) o;
    return Objects.equal(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("id", id)
        .add("alias", alias)
        .add("location", location)
        .toString();
  }
}
