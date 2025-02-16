package tfb.status.service;

import com.google.common.io.MoreFiles;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import tfb.status.config.FileStoreConfig;

/**
 * Provides access to miscellaneous files on the file system that are managed by
 * this application.
 *
 * <p>This class should not refer to any files that are in source control.
 */
@Singleton
public final class FileStore {
  private final Path resultsDirectory;
  private final Path accountsDirectory;
  private final Path shareDirectory;
  private final Path announcementFile;

  /**
   * Constructs a new file store with the provided configuration.
   *
   * @param config the configuration for this file store
   * @param fileSystem the file system to be used
   * @throws IOException if an I/O error occurs while creating the required
   *         directories and files
   */
  @Inject
  public FileStore(FileStoreConfig config, FileSystem fileSystem) throws IOException {
    Objects.requireNonNull(config);
    Objects.requireNonNull(fileSystem);

    Path root = fileSystem.getPath(config.root());
    createDirectoryIfNecessary(root);

    resultsDirectory = root.resolve("results");
    createDirectoryIfNecessary(resultsDirectory);

    accountsDirectory = root.resolve("accounts");
    createDirectoryIfNecessary(accountsDirectory);

    shareDirectory = root.resolve("share");
    createDirectoryIfNecessary(shareDirectory);

    announcementFile = root.resolve("announcement.txt");
    createFileIfNecessary(announcementFile);
  }

  private static void createDirectoryIfNecessary(Path directory) throws IOException {
    Objects.requireNonNull(directory);
    if (!Files.isDirectory(directory)) {
      MoreFiles.createParentDirectories(directory);
      Files.createDirectory(directory);
    }
  }

  private static void createFileIfNecessary(Path file) throws IOException {
    Objects.requireNonNull(file);
    if (!Files.isRegularFile(file)) {
      MoreFiles.createParentDirectories(file);
      Files.createFile(file);
    }
  }

  /**
   * The root directory for uploaded results files.
   */
  public Path resultsDirectory() {
    return resultsDirectory;
  }

  /**
   * The root directory for user account files.
   */
  public Path accountsDirectory() {
    return accountsDirectory;
  }

  /**
   * The root directory for results.json files uploaded by users who want to
   * share their results.
   */
  public Path shareDirectory() {
    return shareDirectory;
  }

  /**
   * The text file containing an announcement to be displayed on the home page.
   */
  public Path announcementFile() {
    return announcementFile;
  }
}
