package me.oczi.bukkit.objects.partnership;

import me.oczi.bukkit.objects.collections.HomeList;
import me.oczi.bukkit.objects.collections.PartnershipPermissionSet;
import me.oczi.bukkit.utils.PartnershipPermission;
import me.oczi.common.api.Emptyble;

import java.util.UUID;

/**
 * A Partnership between two players.
 */
public interface Partnership extends Emptyble {

  /**
   * Check if Partner has permission.
   * @param perm Permission to check
   * @return Has permission or not.
   */
  boolean hasPermission(PartnershipPermission perm);

  /**
   * Set relation of Partnership.
   * @param relation Relation to set.
   */
  void setRelation(String relation);

  /**
   * Get ID of Partnership.
   * @return Partner's ID.
   */
  String getId();

  /**
   * Get first UUID of Partnership.
   * @return Partner's first UUID.
   */
  UUID getUuid1();

  /**
   * Get second UUID of Partnership.
   * @return Partner's second UUID.
   */
  UUID getUuid2();

  /**
   * Get all the permissions of Partnership.
   * @return A EnumSet of permissions.
   */
  PartnershipPermissionSet getPermissions();

  /**
   * Get the relation of Partnership.
   * @return Partner's relation.
   */
  String getRelation();

  /**
   * Get all the homes of Partnership.
   * @return A list of homes.
   */
  HomeList getHomeList();
}
