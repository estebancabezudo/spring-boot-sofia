package net.cabezudo.sofia.sites;

import net.cabezudo.sofia.core.SofiaTemplateEngineEnvironment;
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
  private @Autowired SofiaTemplateEngineEnvironment sofiaTemplateEngineEnvironment;

  public Path getSourcesPath(Site site) throws SourceNotFoundException {
    Path sourcePath = sourcePaths.get(site.getName());

    if (sourcePath == null) {
      for (Path path : sofiaTemplateEngineEnvironment.getSourcePaths()) {
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
    return sofiaTemplateEngineEnvironment.getSitesPath().resolve(site.getName()).resolve(version);
  }

  public Path getVersionedSourcesPath(Site site, String version) {
    return getSourcesPath(site).resolve(version);
  }

  public Path getVersionedSiteTextsPath(Site site, String version) throws SiteNotFoundException, HostNotFoundException {
    return getVersionedSitePath(site, version).resolve(SofiaTemplateEngineEnvironment.TEXTS_FOLDER_NAME);
  }

  public Path getVersionedSiteImagesPath(Site site, String version) throws SiteNotFoundException, HostNotFoundException {
    return getVersionedSitePath(site, version).resolve(SofiaTemplateEngineEnvironment.IMAGES_FOLDER_NAME);
  }

  public Path getVersionedSourcesLibraryPath(Site site, String version) {
    return getSourcesPath(site).resolve(site.getName()).resolve(version).resolve(SofiaTemplateEngineEnvironment.LIBS_DIRECTORY_NAME);
  }
}
