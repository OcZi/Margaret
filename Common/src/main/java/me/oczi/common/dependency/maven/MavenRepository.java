package me.oczi.common.dependency.maven;

public enum MavenRepository {
  JITPACK("https://jitpack.io/"),
  MAVEN("https://repo1.maven.org/maven2/"),
  ASHCON("https://repo.ashcon.app/nexus/content/repositories/snapshots/");

  private final String url;

  MavenRepository(String url) {
    this.url = url;
  }

  public String getUrl() {
    return url;
  }
}
