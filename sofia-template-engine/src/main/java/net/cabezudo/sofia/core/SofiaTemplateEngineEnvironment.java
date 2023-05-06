package net.cabezudo.sofia.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import net.cabezudo.sofia.config.ConfigurationException;
import net.cabezudo.sofia.config.ConfigurationFileYAMLData;
import net.cabezudo.sofia.config.ConfigurationFileYAMLSiteData;
import net.cabezudo.sofia.config.SofiaEnvironment;
import net.cabezudo.sofia.core.modules.ModuleException;
import net.cabezudo.sofia.core.modules.ModuleManager;
import net.cabezudo.sofia.core.modules.SofiaSecurityModule;
import net.cabezudo.sofia.sites.Host;
import net.cabezudo.sofia.sites.SiteManager;
import net.cabezudo.sofia.sites.SiteNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.01.00, 2022.08.30
 */
@Configuration
public class SofiaTemplateEngineEnvironment {

  public static final String DEV = "dev";
  public static final String PROD = "prod";

  public static final String DEFAULT_DIRECTORY_INDEX_FILE = "index.html";
  public static final String SOFIA_DIRECTORY_NAME = "sofia";
  public static final String LIBS_DIRECTORY_NAME = "libs";
  public static final String SITES_DIRECTORY_NAME = "sites";
  public static final String SOURCES_DIRECTORY_NAME = "sources";
  public static final String IMAGES_FOLDER_NAME = "images";
  public static final String TEXTS_FOLDER_NAME = "texts";
  private static final String SOFIA_CONFIGURATION_FILENAME = "sofia.yml";
  private static final Logger log = LoggerFactory.getLogger(SofiaTemplateEngineEnvironment.class);
  private final List<Path> sourcePaths = new ArrayList<>();
  private @Autowired SofiaEnvironment sofiaEnvironment;
  private @Autowired SiteManager siteManager;
  private String name;
  private ConfigurationFileYAMLData configurationFileYAMLData;
  private Path basePath;
  private Path sitesPath;
  private Path systemLibraryPath;
  private @Autowired ModuleManager moduleManager;

  private Path checkForDirectoryAndCreate(String identifier, Path path) throws ConfigurationException, IOException {
    if (!Files.exists(path)) {
      Files.createDirectories(path);
    }
    if (!Files.isDirectory(basePath)) {
      throw new ConfigurationException(identifier + " is not a directory: " + path);
    }
    log.debug(identifier + ": " + basePath);
    return path;
  }

  public Charset getCharset() {
    return StandardCharsets.UTF_8;
  }

  public String getDirectoryIndexFile() {
    return DEFAULT_DIRECTORY_INDEX_FILE;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) throws ConfigurationException {
    if (name == null) {
      throw new ConfigurationException("Invalid environment name: " + null);
    }
    this.name = name;
    log.debug("Sofia environment set to: " + name);
  }

  public boolean isDevelopment() {
    return DEV.equals(name);
  }

  public void setDevelopment(boolean isDevelopment) {
    name = isDevelopment ? DEV : PROD;
  }

  public boolean isProduction() {
    return PROD.equals(name);
  }

  public void loadConfiguration() throws ConfigurationException {
    log.debug("Load configuration for template engine enviroment.");
    try {
      loadDataFromYAMLFile();
    } catch (SiteNotFoundException | ModuleException e) {
      throw new ConfigurationException(e);
    }
    setSystemBasePath();
    log.debug("Sites path: " + getSitesPath());
    setSourcesPaths();
    systemLibraryPath = basePath.resolve(LIBS_DIRECTORY_NAME);
    if (Files.exists(systemLibraryPath) && !Files.isDirectory(systemLibraryPath)) {
      throw new ConfigurationException("The system library path is is not a directory: " + sourcePaths);
    }
    log.debug("System library path: " + getSystemLibraryPath());
  }

  private void setSourcesPaths() throws ConfigurationException {
    String[] sourcesPathsFromData = configurationFileYAMLData.getSourcePaths();
    if (sourcesPathsFromData == null || sourcesPathsFromData.length == 0) {
      sourcePaths.add(basePath.resolve(SOURCES_DIRECTORY_NAME));
    } else {
      for (String sourcePath : sourcesPathsFromData) {
        Path path = Paths.get(sourcePath);
        if (!Files.exists(path)) {
          throw new ConfigurationException("Sources path doesn't exist: " + path);
        }
        if (!Files.isDirectory(path)) {
          throw new ConfigurationException("Sources path is not a directory: " + path);
        }
        log.debug("Found source path: " + path);
        sourcePaths.add(path);
      }
    }
  }

  private void setSystemBasePath() throws ConfigurationException {
    String stringBasePath = configurationFileYAMLData.getBasePath();
    ClassLoader classLoader = this.getClass().getClassLoader();
    if (stringBasePath == null) {
      URL url = classLoader.getResource(SOFIA_DIRECTORY_NAME);
      log.debug("URL: " + url);
      // FIX Posible null pointer
      stringBasePath = url.getFile();
    }
    if (stringBasePath == null) {
      throw new ConfigurationException("Cant find the system path");
    }
    basePath = Paths.get(stringBasePath);
    if (!Files.exists(basePath)) {
      log.warn("Base path doesn't exist: " + basePath);
      log.info("Trying with resources");
      URL url = classLoader.getResource(SOFIA_DIRECTORY_NAME);
      if (url == null) {
        throw new ConfigurationException("Resource for base directory not found: " + SOFIA_DIRECTORY_NAME);
      }

      String urlFile = url.getFile();
      basePath = Paths.get(urlFile);
    }
    if (!Files.isDirectory(basePath)) {
      throw new ConfigurationException("Base path is not a directory: " + basePath);
    }
    log.debug("Base path: " + basePath);

    try {
      sitesPath = checkForDirectoryAndCreate("Sites code", basePath.resolve(SITES_DIRECTORY_NAME));
    } catch (IOException e) {
      throw new ConfigurationException(e);
    }

    if (!Files.isDirectory(sitesPath)) {
      throw new ConfigurationException("Base path is not a directory: " + sitesPath);
    }
  }

  private void loadDataFromYAMLFile() throws SiteNotFoundException, ModuleException {
    log.debug("Load data from configuration file sofia.yml.");
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    ClassLoader classLoader = this.getClass().getClassLoader();
    URL sofiaConfigurationURL = classLoader.getResource(SOFIA_CONFIGURATION_FILENAME);
    if (sofiaConfigurationURL == null) {
      log.debug("Configuration file sofia.yml NOT FOUND in resources path.");
      configurationFileYAMLData = new ConfigurationFileYAMLData();
    } else {
      File sofiaConfigurationFile = new File(sofiaConfigurationURL.getFile());
      try {
        configurationFileYAMLData = mapper.readValue(sofiaConfigurationFile, ConfigurationFileYAMLData.class);
        createHostToSiteData();

        sofiaEnvironment.setSecurityActive(configurationFileYAMLData.isSecurityActive());

        SofiaSecurityModule securityModule = moduleManager.getSecurityModule();
        if (securityModule != null) {
          securityModule.config(configurationFileYAMLData, siteManager);
        }
      } catch (IOException e) {
        log.error("invalid configuration: " + e.getMessage());
      } catch (ConfigurationException e) {
        throw new ModuleException(e);
      }
    }
  }

  private void createHostToSiteData() {
    log.debug("Create host to site data.");
    for (ConfigurationFileYAMLSiteData configurationFileYAMLSiteData : configurationFileYAMLData.getSites()) {
      log.debug("Found data for site: " + configurationFileYAMLSiteData.getName());
      configurationFileYAMLSiteData.getHosts().forEach(hostname -> {
        Host host = new Host(hostname);
        siteManager.add(configurationFileYAMLSiteData.getName(), host);
      });
    }
  }

  public Path getBasePath() {
    return basePath;
  }

  public Path getSitesPath() {
    return sitesPath;
  }

  public List<Path> getSourcePaths() {
    return sourcePaths;
  }

  public Path getSystemLibraryPath() {
    return systemLibraryPath;
  }
}
