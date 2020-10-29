package me.oczi.common.request.github;

import me.oczi.common.api.github.GithubApi;
import me.oczi.common.api.github.GithubRelease;
import me.oczi.common.request.HttpUrlConnectionBuilder;
import me.oczi.common.utils.Gsons;

import java.io.IOException;

public class GithubReleaseResolverImpl implements GithubReleaseResolver {

  @Override
  public GithubRelease[] getReleases(String owner, String repository)
      throws IOException {
    String url = GithubApi.RELEASES.of(owner, repository);
    HttpUrlConnectionBuilder connection = HttpUrlConnectionBuilder.newBuilder(url)
        .setMethod("GET")
        .setConnectionTimeout(8000)
        .setReadTimeout(8000);
    return Gsons.parseConnection(connection, GithubRelease[].class);
  }

  @Override
  public GithubRelease getLatestRelease(String owner, String repository)
      throws IOException {
    GithubRelease[] releases = getReleases(owner, repository);
    return releases.length > 0
        ? releases[0]
        : null;
  }
}
