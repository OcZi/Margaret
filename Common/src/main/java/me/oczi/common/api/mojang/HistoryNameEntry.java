package me.oczi.common.api.mojang;

/**
 * Mojang's History name entry model representation.
 */
public class HistoryNameEntry {
  private String name;
  private long changedToAt;

  public String getName() {
    return name;
  }

  public long getChangedToAt() {
    return changedToAt == 0
        ? System.currentTimeMillis()
        : changedToAt;
  }

  @Override
  public String toString() {
    return "HistoryNameEntry{" +
        "name='" + name + '\'' +
        ", changedToAt=" + getChangedToAt() +
        '}';
  }
}
