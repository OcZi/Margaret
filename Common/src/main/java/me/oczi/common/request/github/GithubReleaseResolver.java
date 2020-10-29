package me.oczi.common.request.github;

import me.oczi.common.api.github.GithubRelease;

import java.io.IOException;

public interface GithubReleaseResolver {

  static GithubReleaseResolver newResolver() {
    return new GithubReleaseResolverImpl();
  }

  GithubRelease[] getReleases(String owner, String repository) throws IOException;

  GithubRelease getLatestRelease(String owner, String repository) throws IOException;

}
