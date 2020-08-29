package me.oczi.common.dependency.monitor;

import me.oczi.common.api.Loggable;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.logging.Logger;

/**
 * A {@link ReadableByteChannel} that can be {@link Loggable}.
 */
public interface MonitorByteChannel extends ReadableByteChannel, Loggable {

  static MonitorByteChannel newChannel(InputStream channel,
                                       String fileName,
                                       int length,
                                       @Nullable Logger logger) {
    return newChannel(Channels.newChannel(channel), fileName, length, logger);
  }

  static MonitorByteChannel newChannel(ReadableByteChannel channel,
                                       String fileName,
                                       int length,
                                       @Nullable Logger logger) {
    return new MonitorByteChannelImpl(channel, fileName, length, logger);
  }

  /**
   * Get the percentage of download to finish.
   * @return Percentage of download
   */
  String getPercentage();

  /**
   * Get the total bytes downloaded.
   * @return Total bytes downloaded.
   */
  int getTotalDownloaded();

  /**
   * Get absolute size of download.
   * @return Absolute file
   */
  int getAbsoluteSize();

  /**
   * Get the needed bytes to download to finish.
   * @return Needed bytes to finish.
   */
  int getBytesNeeded();
}
