package me.oczi.bukkit.objects.collections;

import me.oczi.bukkit.objects.PermissionSet;
import me.oczi.bukkit.utils.PartnerPermission;
import me.oczi.common.utils.BitMasks;

import java.util.EnumSet;
import java.util.Set;

/**
 * A implementation of {@link PermissionSet}
 * for Partner's permission.
 */
public class PartnerPermissionSet extends PermissionSet<PartnerPermission> {

  public PartnerPermissionSet() {
    super(EnumSet.noneOf(PartnerPermission.class));
  }

  public PartnerPermissionSet(int bits) {
    super(bits);
  }

  @Override
  public Set<PartnerPermission> getPermissionsFromBits(int bits) {
    EnumSet<PartnerPermission> permissions =
        EnumSet.noneOf(PartnerPermission.class);
    for (PartnerPermission permission : PartnerPermission.values()) {
      final int ordinal = BitMasks.mask(permission);
      if (BitMasks.bitEquals(bits, ordinal))
        permissions.add(permission);
    }

    return permissions;
  }
}
