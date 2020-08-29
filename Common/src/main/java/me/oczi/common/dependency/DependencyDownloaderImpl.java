package me.oczi.common.dependency;

import me.oczi.common.api.dependency.downloader.DependencyDownloader;
import me.oczi.common.dependency.monitor.MonitorByteChannel;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class DependencyDownloaderImpl implements DependencyDownloader {
  private Logger logger;
  private final File path;

  public DependencyDownloaderImpl(File path) {
    this.path = path;
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  @Override
  public File download(Dependency dependency) {
    Path dependencyPath = Paths.get(
        path.getAbsolutePath(),
        dependency.getFileName());
    File dependencyFile = dependencyPath.toFile();
    try {
      URL url = new URL(dependency.getAbsoluteUrl());
      int expectedSize = getFileSizeOf(url);
      if (dependencyFile.exists()) {
        long fileSize = Files.size(dependencyPath);
        if (fileSize == expectedSize) {
          info(dependency.getFileName() + " [Already exist]");
          return dependencyFile;
        } else {
          warning("Dependency file '" + dependency.getFileName()
                  + "' size not match with file size in Maven repository."
                  + "(File size in folder: " + fileSize
                  + ", File size in repository: " + expectedSize,
              "deleting file to download it again...");
          dependencyFile.delete();
        }
      }

      dependencyFile.createNewFile();
      try (final InputStream inputStream = url.openStream()) {
        try (MonitorByteChannel channel = MonitorByteChannel
            .newChannel(
                inputStream,
                dependency.getFileName(),
                expectedSize,
                logger)) {
          FileOutputStream stream = new FileOutputStream(dependencyFile);
          stream
              .getChannel()
              .transferFrom(channel, 0, expectedSize);
        }
        info("Downloading " + dependency.getFileName() + "... [Success]");
      }
    } catch (FileNotFoundException e) {
      dependencyFile.delete();
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return dependencyFile;
  }

  public int getFileSizeOf(URL url) throws IOException {
    URLConnection connection = url.openConnection();
    return connection.getContentLength();
  }

  @Override
  public void info(String message) {
    if (logger != null) {
      logger.info(message);
    }
  }

  @Override
  public void info(String... messages) {
    if (logger != null) {
      for (String s : messages) {
        logger.info(s);
      }
    }
  }

  @Override
  public void warning(String message) {
    if (logger != null) {
      logger.warning(message);
    }
  }

  @Override
  public void warning(String... messages) {
    if (logger != null) {
      for (String s : messages) {
        logger.warning(s);
      }
    }
  }

  @Override
  public void setLogger(Logger logger) {
    this.logger = logger;
  }
}
