package net.cabezudo.sofia.sites;

import net.cabezudo.sofia.core.SofiaEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;

@Service
public class PathManager {
  public static final String IMAGES_FOLDER_NAME = "images";
  public static final String TEXTS_FOLDER_NAME = "texts";
  public static final String LIBS_DIRECTORY_NAME = "libs";
  private final Map<String, Path> sourcePaths = new TreeMap<>();
  private @Autowired SofiaEnvironment sofiaEnvironment;

  public Path getSourcesPath(Site site) throws SourceNotFoundException {
    Path sourcePath = sourcePaths.get(site.getName());

    if (sourcePath == null) {
      for (Path path : sofiaEnvironment.getSourcePaths()) {
        Path newSourcePath = path.resolve(site.getName());
        if (Files.exists(newSourcePath)) {
          sourcePaths.put(site.getName(), newSourcePath);
          return newSourcePath;
        } else {
          throw new SourceNotFoundException("Source for " + site.getName() + " not found.");
        }
      }
    }
    return sourcePath;
  }

  public Path getVersionedSitePath(Site site, String version) throws SiteNotFoundException, HostNotFoundException {
    return sofiaEnvironment.getSitesPath().resolve(site.getName()).resolve(version);
  }

  public Path getVersionedSourcesPath(Site site, String version) {
    return getSourcesPath(site).resolve(version);
  }

  public Path getVersionedSiteTextsPath(Site site, String version) throws SiteNotFoundException, HostNotFoundException {
    return getVersionedSitePath(site, version).resolve(SofiaEnvironment.TEXTS_FOLDER_NAME);
  }

  public Path getVersionedSiteImagesPath(Site site, String version) throws SiteNotFoundException, HostNotFoundException {
    return getVersionedSitePath(site, version).resolve(SofiaEnvironment.IMAGES_FOLDER_NAME);
  }

  public Path getVersionedSourcesLibraryPath(Site site, String version) {
    return getSourcesPath(site).resolve(version).resolve(SofiaEnvironment.LIBS_DIRECTORY_NAME);
  }
}
