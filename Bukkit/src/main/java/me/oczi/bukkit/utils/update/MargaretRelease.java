package me.oczi.bukkit.utils.update;

import me.oczi.common.api.github.GithubRelease;

import java.time.Instant;

public class MargaretRelease {
  private final String version;
  private final long dateMillis;
  private final boolean preRelease;

  public MargaretRelease(GithubRelease release) {
    this(release.getTagName(),
        // Hack method to parse the Json's DateTime
        // and convert them to milliseconds.
        Instant.parse(
            release.getPublishedDate()).toEpochMilli(),
        release.isPreRelease());
  }

  public MargaretRelease(String version, long dateMillis, boolean preRelease) {
    this.version = version;
    this.dateMillis = dateMillis;
    this.preRelease = preRelease;
  }

  public long getDateMillis() {
    return dateMillis;
  }

  public String getVersion() {
    return version;
  }
}
