package me.oczi.bukkit.utils.update;

import me.oczi.bukkit.other.exceptions.CheckUpdateException;

public interface MargaretVersionChecker {

  static MargaretVersionChecker newChecker(String actualVersion) {
    return new MargaretVersionCheckerImpl(actualVersion);
  }

  MargaretRelease checkUpdate() throws CheckUpdateException;

  boolean hasUpdate();

  MargaretRelease getRelease();
}
