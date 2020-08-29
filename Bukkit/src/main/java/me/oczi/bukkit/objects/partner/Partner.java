package me.oczi.bukkit.objects.partner;

import me.oczi.bukkit.objects.collections.HomeList;
import me.oczi.bukkit.objects.collections.PartnerPermissionSet;
import me.oczi.bukkit.utils.PartnerPermission;
import me.oczi.common.api.Emptyble;

import java.util.UUID;

/**
 * A interpretation of a Relationship.
 */
public interface Partner extends Emptyble {

  /**
   * Check if Partner has permission.
   * @param perm Permission to check
   * @return Has permission or not.
   */
  boolean hasPermission(PartnerPermission perm);

  /**
   * Set relation of Partner.
   * @param relation Relation to set.
   */
  void setRelation(String relation);

  /**
   * Get ID of Partner.
   * @return Partner's ID.
   */
  String getId();

  /**
   * Get first UUID of Partner.
   * @return Partner's first UUID.
   */
  UUID getUuid1();

  /**
   * Get second UUID of Partner.
   * @return Partner's second UUID.
   */
  UUID getUuid2();

  /**
   * Get all the permissions of Partner.
   * @return A EnumSet of permissions.
   */
  PartnerPermissionSet getPermissions();

  /**
   * Get the relation of Partner.
   * @return Partner's relation.
   */
  String getRelation();

  /**
   * Get all the homes of Partner.
   * @return A list of homes.
   */
  HomeList getHomeList();
}
