package me.oczi.bukkit.utils;

import me.oczi.bukkit.objects.player.MargaretPlayer;
import me.oczi.bukkit.utils.settings.BasicSetting;
import org.bukkit.entity.Player;

/**
 * Utils related to sound.
 */
public interface SoundUtils {

  static void playSound(VersionSound sound,
                        MargaretPlayer... players) {
    for (MargaretPlayer player : players) {
      playSound(player, sound);
    }
  }

  static void playSound(MargaretSound sound,
                        MargaretPlayer... players) {
    for (MargaretPlayer player : players) {
      playSound(player, sound);
    }
  }

  static void playSound(MargaretPlayer player,
                        MargaretSound sound) {
    if (player.isSetting(BasicSetting.SOUND_EFFECTS)) {
      playSound(MargaretPlayers.getAsPlayer(player),
          sound);
    }
  }

  static void playSound(MargaretPlayer player,
                        VersionSound sound) {
    if (player.isSetting(BasicSetting.SOUND_EFFECTS)) {
      playSound(MargaretPlayers.getAsPlayer(player),
          sound);
    }
  }

  static void playSound(VersionSound sound,
                        Player... players) {
    for (Player player : players) {
      playSound(player, sound);
    }
  }

  static void playSound(MargaretSound sound,
                        Player... players) {
    for (Player player : players) {
      playSound(player, sound);
    }
  }

  static void playSound(Player player,
                        MargaretSound sound) {
    playSound(player, sound.getVersionSound());
  }

  static void playSound(Player player,
                        VersionSound sound) {
    player.playSound(player.getLocation(),
        sound.getBukkitSound(),
        4,
        0);
  }
}
