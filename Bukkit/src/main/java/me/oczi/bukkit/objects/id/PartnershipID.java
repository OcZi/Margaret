package me.oczi.bukkit.objects.id;

import me.oczi.bukkit.objects.partnership.Partnership;

/**
 * {@link ID} implementation with {@link ApacheID}
 * as delegate to generate
 * {@link Partnership}'s ID.
 */
public class PartnershipID implements ID{
  private final ID delegate;

  public PartnershipID() {
    this.delegate = new ApacheID(8);
  }

  @Override
  public void generateNewId() {
    delegate.generateNewId();
  }

  @Override
  public String getID() {
    return delegate.getID();
  }
}
