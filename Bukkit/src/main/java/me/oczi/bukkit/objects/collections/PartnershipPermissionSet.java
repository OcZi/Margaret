package me.oczi.bukkit.objects.collections;

import me.oczi.bukkit.utils.PartnershipPermission;
import me.oczi.common.utils.BitMasks;

import java.util.EnumSet;
import java.util.Set;

/**
 * A implementation of {@link PermissionSet}
 * for Partner's permission.
 */
public class PartnershipPermissionSet extends PermissionSet<PartnershipPermission> {

  public PartnershipPermissionSet() {
    super(EnumSet.noneOf(PartnershipPermission.class));
  }

  public PartnershipPermissionSet(int bits) {
    super(bits);
  }

  @Override
  public Set<PartnershipPermission> getPermissionsFromBits(int bits) {
    EnumSet<PartnershipPermission> permissions =
        EnumSet.noneOf(PartnershipPermission.class);
    for (PartnershipPermission permission : PartnershipPermission.values()) {
      final int ordinal = BitMasks.mask(permission);
      if (BitMasks.bitEquals(bits, ordinal))
        permissions.add(permission);
    }

    return permissions;
  }
}
