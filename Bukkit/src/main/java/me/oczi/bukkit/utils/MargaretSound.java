package me.oczi.bukkit.utils;

public enum MargaretSound {
  PROPOSAL(VersionSound.ORB_PICKUP),
  NOTIFICATION(VersionSound.NOTE_PLING),
  ERROR(VersionSound.BURP);

  private final VersionSound sound;

  MargaretSound(VersionSound sound) {
    this.sound = sound;
  }

  public VersionSound getVersionSound() {
    return sound;
  }
}
