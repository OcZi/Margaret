package me.oczi.common.dependency;

import me.oczi.common.dependency.monitor.MonitorByteChannel;
import me.oczi.common.request.HttpUrlConnectionBuilder;

import java.io.*;
import java.net.HttpURLConnection;
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
      HttpURLConnection connection =
          newConnection(dependency.getAbsoluteUrl());
      int expectedSize = connection.getContentLength();
      String dependencyName = dependency.getArtifactId();
      if (dependencyFile.exists()) {
        long fileSize = Files.size(dependencyPath);
        if (fileSize == expectedSize) {
          info(dependencyName + " [Already exist]");
          return dependencyFile;
        } else {
          if (expectedSize == -1) {
            throw new IOException(
                String.format(
                    "Dependency file '%s' not found in %s's repository " +
                        "(Repository offline or server without internet?)",
                    dependency.getArtifactId(),
                    dependency.getMavenRepository()));
          }
          warning(
              String.format(
                  "Dependency file '%s' size not match with file size in %s's repository " +
                      "(File size in folder: %d, File size in repository: %d",
                  dependency.getFileName(),
                  dependency.getMavenRepository(),
                  fileSize,
                  expectedSize),
              "deleting file to download it again...");
          dependencyFile.delete();
        }
      }

      dependencyFile.createNewFile();
      try (final InputStream inputStream = connection.getInputStream()) {
        try (MonitorByteChannel channel = MonitorByteChannel
            .newChannel(
                inputStream, dependencyName,
                expectedSize, logger)) {
          FileOutputStream stream = new FileOutputStream(dependencyFile);
          stream
              .getChannel()
              .transferFrom(channel, 0, expectedSize);
        }
        info(
            String.format(
            "Downloading %s... [Success]",
                dependencyName));
      }
    } catch (FileNotFoundException e) {
      dependencyFile.delete();
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return dependencyFile;
  }

  private HttpURLConnection newConnection(String url) {
    return HttpUrlConnectionBuilder.newBuilder(url)
        .setProperty("User-Agent", "Margaret")
        .setReadTimeout(10000)
        .setConnectionTimeout(10000)
        .build();
  }

  private void info(String message) {
    if (logger != null) {
      logger.info(message);
    }
  }

  private void info(String... messages) {
    if (logger != null) {
      for (String s : messages) {
        info(s);
      }
    }
  }

  private void warning(String message) {
    if (logger != null) {
      logger.warning(message);
    }
  }

  private void warning(String... messages) {
    if (logger != null) {
      for (String s : messages) {
        warning(s);
      }
    }
  }

  @Override
  public void setLogger(Logger logger) {
    this.logger = logger;
  }
}
