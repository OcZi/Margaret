package me.oczi.bukkit.objects.id;

/**
 * {@link ID} implementation with {@link ApacheID}
 * as delegate to generate
 * {@link me.oczi.bukkit.objects.partner.Partner}'s ID.
 */
public class PartnerID implements ID{
  private final ID delegate;

  public PartnerID() {
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
