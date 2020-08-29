package me.oczi.bukkit.objects.id;

/**
 * {@link ID} implementation with {@link ApacheID}
 * as delegate to generate
 * {@link me.oczi.bukkit.objects.Home}'s ID.
 */
public class HomeID implements ID{
  private final ID delegate;

  public HomeID() {
    this.delegate = new ApacheID(12);
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
