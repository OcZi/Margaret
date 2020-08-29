package me.oczi.common.dependency;

public enum DependencyUrlFormat {
  JAR("%s-%s.jar"),

  MAVEN_URL("%s/%s/%s/");

  private final String format;

  DependencyUrlFormat(String format) {
    this.format = format;
  }

  public String getFormat() {
    return format;
  }
}
