package me.oczi.bukkit.utils;

import me.oczi.bukkit.other.exceptions.SoundNotFoundException;
import me.oczi.common.utils.CommonsUtils;
import org.bukkit.Sound;

/**
 * List of the sounds used in the plugin.
 * Based on: https://gist.github.com/NiklasEi/7bd0ffd136f8459df0940e4501d47a8a
 */
public enum VersionSound {
  ORB_PICKUP("ORB_PICKUP", "ENTITY_EXPERIENCE_ORB_PICKUP"),
  LEVEL_UP("LEVEL_UP", "ENTITY_PLAYER_LEVELUP"),
  BURP("BURP", "ENTITY_PLAYER_BURP"),
  NOTE_PLING("NOTE_PLING", "BLOCK_NOTE_PLING", "BLOCK_NOTE_BLOCK_PLING");

  private final String[] sounds;
  private Sound cache;

  VersionSound(String... sounds) {
    this.sounds = sounds;
  }

  public Sound getBukkitSound() {
    return loadSound();
  }

  public Sound loadSound() {
    if (cache != null) {
      return cache;
    } else {
      for (String sound : sounds) {
        if (CommonsUtils.enumExist(sound, Sound.class)) {
          return cache = Sound.valueOf(sound);
        }
      }
    }

    throw new SoundNotFoundException(
        "Sound " + name() + " not found in org.bukkit.Sound");
  }
}
