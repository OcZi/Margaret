package me.oczi.common.dependency;

import me.oczi.common.dependency.maven.MavenRepository;

/**
 * Dependency interface representation.
 */
public interface Dependency {

  /**
   * Get Dependency's group ID.
   * @return Group ID.
   */
  String getGroupId();

  /**
   * Get Dependency's artifact ID.
   * @return Artifact ID.
   */
  String getArtifactId();

  /**
   * Get Dependency's version.
   * @return Version.
   */
  String getVersion();

  /**
   * Get checksum of dependency.
   * @return Checksum.
   */
  String getChecksum();

  /**
   * Get URL of dependency.
   * @return URL.
   */
  String getUrl();

  /**
   * Get the file name of dependency.
   * @return File name.
   */
  String getFileName();

  /**
   * Get the url with the Maven repository and file name.
   * @return Absolute URL
   */
  String getAbsoluteUrl();

  /**
   * Get maven repository that
   * @return
   */
  MavenRepository getMavenRepository();
}
