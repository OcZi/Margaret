package me.oczi.bukkit.objects.id;

import org.apache.commons.lang.RandomStringUtils;

/**
 * {@link ID} implementation
 * with Apache's {@link RandomStringUtils}.
 */
public final class ApacheID implements ID {
  private final int stringSize;
  private String id;

  public ApacheID(int i) {
    this.stringSize = i;
    generateNewId();
  }

  @Override
  public void generateNewId() {
    this.id = RandomStringUtils
        .randomAlphanumeric(stringSize);
  }

  @Override
  public String getID() {
    return id;
  }
}
