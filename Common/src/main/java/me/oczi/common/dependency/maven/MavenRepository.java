package me.oczi.common.dependency.maven;

/**
 * URL of Maven's repository.
 */
public enum MavenRepository {
  MAVEN("https://repo1.maven.org/maven2/"),
  UNNAMED_RELEASES("https://repo.unnamed.team/repository/unnamed-releases/");

  private final String url;

  MavenRepository(String url) {
    this.url = url;
  }

  public String getUrl() {
    return url;
  }
}
