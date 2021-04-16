package me.oczi.bukkit.utils.lib;

import com.google.common.collect.Lists;
import me.oczi.common.dependency.Dependency;
import me.oczi.common.dependency.DependencyUrlFormat;
import me.oczi.common.dependency.maven.MavenRepository;
import me.oczi.common.utils.CommonsUtils;

import java.util.List;

/**
 * A plugin's libraries interpretation.
 * Used for download and load in runtime.
 */
public enum MargaretLibrary implements Dependency {
  // Connection pool and dependencies
  HIKARICP("com.zaxxer",
      "HikariCP",
      "3.4.5",
      "i3MvlHBXDUqEHcHvbIJrWGl4sluoMHEv8fpZ3idd+mE=",
      MavenRepository.MAVEN),
  SLF4J_API("org.slf4j",
      "slf4j-api",
      "1.7.30",
      "zboHlk0btAoHYUhcax6ML4/Z6x0ZxTkorA1/lRAQXFc=",
      MavenRepository.MAVEN),
  SLF4J_SIMPLE("org.slf4j",
      "slf4j-simple",
      "1.7.30",
      "i5J5y/9rn4hZTvrjzwIDm2mVAw7sAj7UOSh0jEFnD+4=",
      MavenRepository.MAVEN),

  // Command Framework
  COMMANDFLOW_UNIVERSAL("me.fixeddev",
      "commandflow-universal",
      "0.4.3",
      "tCN3RYO4ZjfK3UmZPCkof3UPhA0PlAuPEPKO0Ewd6gw=",
      MavenRepository.UNNAMED_RELEASES),
  COMMANDFLOW_BUKKIT("me.fixeddev",
      "commandflow-bukkit",
      "0.4.0",
      "qkiCGkpA5Let02/0M2MjWUAmtOwyFK9RTHIzVGV/+Q8=",
      MavenRepository.UNNAMED_RELEASES),

  // Text Framework
  TEXT_API("net.kyori",
      "text-api",
      "3.0.4",
      "qJCoD0fTnRhI0EpqdiLAT9QH5gIyY8aNw4Exe/gTWm0=",
      MavenRepository.MAVEN),
  TEXT_BUKKIT_ADAPTER("net.kyori",
      "text-adapter-bukkit",
      "3.0.5",
      "cXA/7PDtnWpd8l7H4AEhP/3Z/WRNiFhDSqKbqO/1+ig=",
      MavenRepository.MAVEN),
  TEXT_SERIALIZER_GSON("net.kyori",
      "text-serializer-gson",
      "3.0.4",
      "pes03k1/XKS9OpiK+xqVmk+lXSJIsCEkkg3g36PV65A=",
      MavenRepository.MAVEN),
  TEXT_SERIALIZER_LEGACY("net.kyori",
      "text-serializer-legacy",
      "3.0.4",
      "1ZYqzZ7zhnN2AyU/n/NeRQv0A9R01j/gX1Uq/nE02SI=",
      MavenRepository.MAVEN),

  // Dependencies
  /*
  CHECKER_QUAL("org.checkerframework",
      "checker-qual",
      "2.5.8",
      "vWVdpTXfHuuW/Ih00F0PcCkgnelQluT4S9B8ZmiKIQ0=",
      MavenRepository.MAVEN),
  */
  CAFFEINE("com.github.ben-manes.caffeine",
      "caffeine",
      "2.8.5",
      "gUsVqb9Zjg+oVN1wup9uA6QTqXl53gw/STFyleQ1K8g=",
      MavenRepository.MAVEN),

  // Database drivers
  H2("com.h2database",
      "h2",
      "1.4.199",
      "MSWhZ0O8a0z7thq7p4MgPx+2gjCqD9yXiY95b5ml1C4=",
      MavenRepository.MAVEN),
  HSQLDB("org.hsqldb",
      "hsqldb",
      "2.5.1",
      "oIu+EYfPcltyGlNuzDy4JTF6iDYW1sTvJdVkpwrYMgI=",
      MavenRepository.MAVEN),
  POSTGRESQL("org.postgresql",
      "postgresql",
      "42.2.14",
      "SLu6BYRbQLzOZuzj12UhU9J7U3nVrpCXe3ju/Xx6Aoc=",
      MavenRepository.MAVEN),
  MARIADB("org.mariadb.jdbc",
      "mariadb-java-client",
      "2.6.1",
      "Qel071UCZivIZz5M5VF8LOGQRp5/6ewkDVk6bLHOssk=",
      MavenRepository.MAVEN);

  private final String groupId;
  private final String artifactId;
  private final String version;
  private final String snapshot;
  private final String checksum;
  private final MavenRepository mavenRepository;

  MargaretLibrary(String groupId,
                  String artifactId,
                  String version,
                  String checksum,
                  MavenRepository mavenRepository) {
    this(groupId,
        artifactId,
        version,
        "",
        checksum,
        mavenRepository);
  }

  MargaretLibrary(String groupId,
                  String artifactId,
                  String version,
                  String snapshot,
                  String checksum,
                  MavenRepository mavenRepository) {
    this.groupId = groupId;
    this.artifactId = artifactId;
    this.version = version;
    this.snapshot = snapshot;
    this.checksum = checksum;
    this.mavenRepository = mavenRepository;
  }

  @Override
  public String getGroupId() {
    return groupId;
  }

  @Override
  public String getArtifactId() {
    return artifactId;
  }

  @Override
  public String getVersion() {
    return version;
  }

  @Override
  public String getChecksum() {
    return checksum;
  }

  @Override
  public String getFileName() {
    Object[] strings = snapshot.isEmpty()
        ? new String[]{artifactId, version}
        : new String[]{artifactId, snapshot};
    return String.format(
        DependencyUrlFormat.JAR.getFormat(),
        strings);
  }

  @Override
  public String getUrl() {
    return String.format(
        DependencyUrlFormat.MAVEN_URL.getFormat(),
        CommonsUtils.formatUrl(groupId),
        CommonsUtils.formatUrl(artifactId),
        version);
  }

  @Override
  public String getAbsoluteUrl() {
    return mavenRepository.getUrl() +
        getUrl() +
        getFileName();
  }

  @Override
  public MavenRepository getMavenRepository() {
    return mavenRepository;
  }

  public static List<Dependency> getDefaultDependencies() {
    return Lists.newArrayList(
        HIKARICP,
        SLF4J_API,
        SLF4J_SIMPLE,
        CAFFEINE,
        COMMANDFLOW_UNIVERSAL,
        COMMANDFLOW_BUKKIT,
        TEXT_API,
        TEXT_BUKKIT_ADAPTER,
        TEXT_SERIALIZER_GSON,
        TEXT_SERIALIZER_LEGACY);
  }
}
