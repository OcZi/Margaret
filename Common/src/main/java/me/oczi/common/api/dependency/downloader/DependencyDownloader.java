package me.oczi.common.api.dependency.downloader;

import me.oczi.common.dependency.maven.MavenDependency;

import java.io.File;
import java.util.logging.Logger;

public interface DependencyDownloader {

  File download(MavenDependency dependency);

  void setLogger(Logger logger);
}
