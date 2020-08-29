package me.oczi.common.dependency;

import me.oczi.common.api.Loggable;

import java.io.File;
import java.nio.channels.FileChannel;
import java.util.logging.Logger;

/**
 * Downloader of dependencies.
 */
public interface DependencyDownloader extends Loggable {

  /**
   * Download dependency with {@link FileChannel}
   * @param dependency Dependency to download.
   * @return File downloaded.
   */
  File download(Dependency dependency);

  /**
   * Set logger of download.
   * @param logger Logger.
   */
  void setLogger(Logger logger);
}
