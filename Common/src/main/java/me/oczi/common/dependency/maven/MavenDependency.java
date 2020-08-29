package me.oczi.common.dependency.maven;

import me.oczi.common.dependency.Dependency;

public class MavenDependency {
  private final String absoluteUrl;
  private final String fileName;

  private final String groupID;
  private final String artifactId;
  private final String version;

  private final String checksum;

  public MavenDependency(Dependency dependency) {
    this.groupID = dependency.getGroupId();
    this.artifactId = dependency.getArtifactId();
    this.version = dependency.getVersion();

    this.fileName = dependency.getFileName();
    this.absoluteUrl = dependency.getAbsoluteUrl();
    this.checksum = dependency.getChecksum();
  }

  public String getGroupID() {
    return groupID;
  }

  public String getArtifactId() {
    return artifactId;
  }

  public String getVersion() {
    return version;
  }

  public String getAbsoluteUrl() {
    return absoluteUrl;
  }

  public String getFileName() {
    return fileName;
  }

  public String getChecksum() {
    return checksum;
  }
}
