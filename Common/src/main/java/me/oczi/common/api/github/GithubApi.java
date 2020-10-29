package me.oczi.common.api.github;

public enum GithubApi {
  RELEASES("https://api.github.com/repos/%s/%s/releases");

  private final String url;

  GithubApi(String url) {
    this.url = url;
  }

  public String getUrl() {
    return url;
  }

  public String of(String owner,
                   String repository) {
    return String.format(url, owner, repository);
  }
}
