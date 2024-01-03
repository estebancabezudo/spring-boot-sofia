package net.cabezudo.sofia.files;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.01.00, 2022.09.03
 */
public class FileHelper {
  private static final Logger log = LoggerFactory.getLogger(FileHelper.class);

  private FileHelper() {
    // Utility classes should not have public constructors
  }

  public static Path getConfigurationFile(Path file) {
    return Paths.get(FileHelper.removeExtension(file) + ".json");
  }

  public static Path removeExtension(Path filenamePath) {
    return removeExtension(filenamePath.toString());
  }

  public static Path removeExtension(String filename) {
    int i = filename.lastIndexOf(".");
    if (i < 0) {
      return Paths.get(filename);
    }
    return Paths.get(filename.substring(0, i));
  }

  public static String getExtension(Path filenamePath) {
    return getExtension(filenamePath.toString());
  }

  public static String getExtension(String filename) {
    return filename.substring(filename.lastIndexOf(".") + 1);
  }

  public static void copyFolder(Path source, Path target, CopyOption... options) throws IOException {
    Files.walkFileTree(source, new SimpleFileVisitor<Path>() {
      @Override
      public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        Files.createDirectories(target.resolve(source.relativize(dir)));
        return FileVisitResult.CONTINUE;
      }

      @Override
      public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        Path targetPath = target.resolve(source.relativize(file));
        log.debug("Copy files from " + file + " to " + targetPath);
        Files.copy(file, targetPath, options);
        return FileVisitResult.CONTINUE;
      }
    });
  }
}
