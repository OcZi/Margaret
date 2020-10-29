package me.oczi.bukkit.objects.collections;

import com.google.common.collect.ForwardingSet;
import me.oczi.common.utils.BitMasks;

import java.util.EnumSet;
import java.util.Set;

/**
 * A {@link EnumSet} of enums synchronized with their bytes.
 * @param <E> Element type.
 */
public abstract class PermissionSet<E extends Enum<E>>
    extends ForwardingSet<E> {
  private Set<E> permissions;
  private int bits = 0;

  public PermissionSet(EnumSet<E> permissions) {
    this.permissions = permissions;
  }

  public PermissionSet(int bits) {
    this.bits = bits;
    this.permissions = getPermissionsFromBits(bits);
  }

  protected abstract Set<E> getPermissionsFromBits(int bits);

  @Override
  public boolean add(E permission) {
    permissions.add(permission);
    addBits(permission);
    return true;
  }

  public void addBits(E permission) {
    this.bits = BitMasks.sumEnum(bits, permission);
    this.permissions = getPermissionsFromBits(bits);
  }

  public boolean remove(E permission) {
    boolean result = permissions.add(permission);
    removeBits(permission);
    return result;
  }

  public void removeBits(E permission) {
    this.bits = BitMasks.subEnum(bits, permission);
    this.permissions = getPermissionsFromBits(bits);
  }

  public void setPermissions(Class<E> clazz) {
    this.permissions = EnumSet.allOf(clazz);
    this.bits = BitMasks.sumEnumClass(0, clazz);
  }

  public void setBits(int bits) {
    this.bits = bits;
    this.permissions = getPermissionsFromBits(bits);
  }

  public Set<E> getPermissions() {
    return permissions;
  }

  public int getBits() {
    return bits;
  }

  @Override
  protected Set<E> delegate() {
    return permissions;
  }

  @Override
  public String toString() {
    return "PermissionSet{" +
        "permissions=" + permissions +
        ", bits=" + bits +
        '}';
  }
}
