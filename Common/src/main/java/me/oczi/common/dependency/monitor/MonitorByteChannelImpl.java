package me.oczi.common.dependency.monitor;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * Implementation of {@link MonitorByteChannel}.
 */
public class MonitorByteChannelImpl implements MonitorByteChannel {
  private final String fileName;
  // Hardcoded message info
  private final String formatInfo = "Downloading %s... [%s]";

  private final ReadableByteChannel delegate;
  private final int expectedSize;
  private int downloadSize;
  private Logger logger;

  private final int ticks = 50;
  private final AtomicInteger cooldownTicks = new AtomicInteger(ticks);

  public MonitorByteChannelImpl(ReadableByteChannel delegate,
                                String fileName,
                                int expectedSize,
                                @Nullable Logger logger) {
    this.fileName = fileName;
    this.delegate = delegate;
    this.expectedSize = expectedSize;
    setLogger(logger);
  }

  @Override
  public int read(ByteBuffer byteBuffer) throws IOException {
    int bytesDownloaded = delegate.read(byteBuffer);
    if (bytesDownloaded > 0) {
      downloadSize += bytesDownloaded;
      if (cooldownTicks.get() > 0) {
        cooldownTicks.getAndDecrement();
      } else {
        info(String.format(formatInfo, fileName, getPercentage()));
        cooldownTicks.set(ticks);
      }
    }
    return bytesDownloaded;
  }

  @Override
  public boolean isOpen() {
    return delegate.isOpen();
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
    if (logger != null) {
      this.logger = logger;
    }
  }

  @Override
  public void close() throws IOException {
    delegate.close();
  }

  @Override
  public String getPercentage() {
    long result = Math.round(
        ((double) downloadSize / (double) expectedSize) * 100.0);
    return result + "%";
  }

  @Override
  public int getTotalDownloaded() {
    return downloadSize;
  }

  @Override
  public int getAbsoluteSize() {
    return expectedSize;
  }

  @Override
  public int getBytesNeeded() {
    return expectedSize - downloadSize;
  }
}
