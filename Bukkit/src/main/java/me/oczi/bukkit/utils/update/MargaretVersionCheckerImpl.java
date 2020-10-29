package me.oczi.bukkit.utils.update;

import me.oczi.bukkit.other.exceptions.CheckUpdateException;
import me.oczi.common.api.github.GithubRelease;
import me.oczi.common.request.github.GithubReleaseResolver;
import me.oczi.common.utils.CommonsUtils;

import static com.google.common.base.Preconditions.checkNotNull;

public class MargaretVersionCheckerImpl implements MargaretVersionChecker {
  private String actualVersion;

  private final GithubReleaseResolver resolver;
  private MargaretRelease release;

  private boolean update;

  MargaretVersionCheckerImpl(String actualVersion) {
    this.actualVersion = actualVersion;
    this.resolver = GithubReleaseResolver.newResolver();
  }

  @Override
  public MargaretRelease checkUpdate() throws CheckUpdateException {
    try {
      GithubRelease latestRelease = resolver.getLatestRelease("OcZi", "Margaret");
      this.release = new MargaretRelease(
          checkNotNull(latestRelease));
    } catch (Exception e) {
      throw new CheckUpdateException("Cannot check new updates from Github's repository", e);
    }
    int checkVersion = CommonsUtils.filterIntegers(release.getVersion());
    int actualVersion = CommonsUtils.filterIntegers(this.actualVersion);
    this.update = checkVersion > actualVersion;
    return release;
  }

  @Override
  public boolean hasUpdate() {
    return release != null && update;
  }

  @Override
  public MargaretRelease getRelease() {
    return release;
  }
}
