package me.oczi.common.dependency;

import com.google.common.collect.Lists;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import me.oczi.common.utils.CommonsUtils;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Logger;

/**
 * Implementation of {@link DependencyManager}.
 */
public class DependencyManagerImpl implements DependencyManager {
  private File path;

  private final DependencyDownloader downloader;
  private final DependencyResolver resolver;
  private final List<Dependency> dependencyList = new ArrayList<>();
  private Logger logger;
  private boolean offlineMode = false;

  public DependencyManagerImpl(File path, ClassLoader classLoader) {
    this(path, classLoader, null);
  }

  public DependencyManagerImpl(File path, ClassLoader classLoader, @Nullable Logger logger) {
    if (!(classLoader instanceof URLClassLoader)) {
      throw new ClassCastException("ClassLoader is not instance of URLClassLoader.");
    }

    this.resolver = new DependencyResolverImpl((URLClassLoader) classLoader);
    this.downloader = new DependencyDownloaderImpl(path);
    setLogger(logger);
    setPath(path);
  }

  @Override
  public DependencyManager addDependency(Dependency dependency) {
    dependencyList.add(dependency);
    return this;
  }

  @Override
  public DependencyManager addDependency(Dependency... dependencies) {
    for (Dependency dependency : dependencies) {
      addDependency(dependency);
    }
    return this;
  }

  @Override
  public DependencyManager addDependency(Collection<Dependency> dependencies) {
    for (Dependency dependency : dependencies) {
      addDependency(dependency);
    }
    return this;
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  @Override
  public List<Dependency> process()
      throws IOException,
      InvocationTargetException,
      IllegalAccessException {
    if (dependencyList.isEmpty()) {
      info("Dependency list is empty...");
      return Collections.emptyList();
    }

    if (!path.exists()) {
      path.mkdirs();
    }

    List<File> files = downloadAll();
    for (File file : files) {
      resolver.addUrl(file.toURI().toURL());
    }

    info("Downloading and loading dependencies successfully.");
    return getResultAndClear();
  }

  private List<Dependency> getResultAndClear() {
    // Clone dependencyList.
    List<Dependency> result = Lists.newArrayList(dependencyList);
    dependencyList.clear();
    return result;
  }

  private List<File> downloadAll() throws IOException {
    List<File> files = new ArrayList<>();
    info("Start to download dependencies... ["
        + dependencyList.size() + " dependencies]");
    for (Dependency dependency : dependencyList) {
      // Download and load dependency in lib folder
      File fileOutput = offlineMode
          ? getFileDirectly(dependency)
          : downloader.download(dependency);
      Map<String, File> fileMap =
          CommonsUtils.mapPath(fileOutput.getParent());
      if (!fileMap.containsKey(dependency.getFileName())) {
        throw new IOException(
            String.format("Dependency file %s doesn't exist.",
                dependency.getFileName()));
      }
      checkHash(fileOutput, dependency.getChecksum());
      files.add(fileOutput);
    }
    return files;
  }

  private File getFileDirectly(Dependency dependency) {
    return Paths.get(
        path.getAbsolutePath(),
        dependency.getFileName()).toFile();
  }

  @Override
  public void setPath(File path) {
    if (path == null || path.toString().isEmpty()) {
      throw new NullPointerException("Path is null or empty.");
    }
    this.path = path;
  }

  @Override
  public List<Dependency> getDependencyList() {
    return dependencyList;
  }

  /**
   * Check hash of file with checksum.
   *
   * @param file     File to bytes.
   * @param checksum SHA-256 encoded Base64 to bytes.
   * @throws IOException If hash throw a error or File's hash not equals to checksum.
   */
  @SuppressWarnings("UnstableApiUsage")
  private File checkHash(File file, String checksum)
      throws IOException {
    HashCode hashCode = Files
        .asByteSource(file)
        .hash(Hashing.sha256());
    byte[] fileBytes = hashCode.asBytes();
    byte[] requiredBytes = Base64.getDecoder().decode(checksum);
    if (!Arrays.equals(fileBytes, requiredBytes)) {
      throw new IOException(
          file.getName() + "'s Checksum not match.");
    }
    return file;
  }

  private void info(String message) {
    if (logger != null) {
      logger.info(message);
    }
  }

  private void warning(String message) {
    if (logger != null) {
      logger.warning(message);
    }
  }

  @Override
  public void setLogger(Logger logger) {
    if (logger != null) {
      this.logger = logger;
      downloader.setLogger(logger);
    }
  }

  @Override
  public void setOfflineMode(boolean offlineMode) {
    this.offlineMode = offlineMode;
    if (offlineMode) {
      info("Dependency Offline-mode activated.");
    }
  }
}
