package me.oczi.bukkit.objects.id;

/**
 * A ID generator.
 */
public interface ID {

  /**
   * Generate a new ID.
   */
  void generateNewId();

  /**
   * Get ID generated.
   * @return ID.
   */
  String getID();
}
