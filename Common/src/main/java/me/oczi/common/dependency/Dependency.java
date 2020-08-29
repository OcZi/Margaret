package me.oczi.common.dependency;

import me.oczi.common.dependency.maven.MavenRepository;

public interface Dependency {

  String getGroupId();

  String getArtifactId();

  String getVersion();

  String getChecksum();

  String getUrl();

  String getFileName();

  String getAbsoluteUrl();

  MavenRepository getMavenRepository();
}
