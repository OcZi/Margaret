package me.oczi.common.api.dependency.downloader;

import me.oczi.common.api.Loggable;
import me.oczi.common.dependency.Dependency;

import java.io.File;
import java.util.logging.Logger;

/**
 *
 */
public interface DependencyDownloader extends Loggable {

  File download(Dependency dependency);

  void setLogger(Logger logger);
}
