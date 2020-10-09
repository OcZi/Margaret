package me.oczi.common.api.mojang;

/**
 * Mojang's Account model representation.
 */
public class MojangAccount {
  private String name;
  private String id;

  public void setName(String name) {
    this.name = name;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public String getId() {
    return id;
  }

  @Override
  public String toString() {
    return "MojangAccountEntry{" +
        "name='" + name + '\'' +
        ", id='" + id + '\'' +
        '}';
  }
}
