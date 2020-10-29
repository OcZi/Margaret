package me.oczi.common.api.github;

public class GithubRelease {
  // Yes, have a really bad names
  // but it necessary for Gson's parsing.
  private String name;
  private String tag_name;

  private String published_at;
  private boolean prerelease;

  public String getName() {
    return name;
  }

  public String getTagName() {
    return tag_name;
  }

  public String getPublishedDate() {
    return published_at;
  }

  public boolean isPreRelease() {
    return prerelease;
  }

  @Override
  public String toString() {
    return "GithubRelease{" +
        "name='" + name + '\'' +
        ", tagName='" + tag_name + '\'' +
        ", publishedDate=" + published_at +
        ", preRelease=" + prerelease +
        '}';
  }
}
