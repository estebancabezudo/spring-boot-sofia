package net.cabezudo.sofia.core;

import lombok.Getter;
import lombok.Setter;
import net.cabezudo.sofia.config.ConfigurationException;
import net.cabezudo.sofia.config.ConfigurationFileYAMLData;
import net.cabezudo.sofia.config.ConfigurationFileYAMLSiteData;
import net.cabezudo.sofia.sites.Host;
import net.cabezudo.sofia.sites.service.SiteManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.01.00, 2022.08.30
 */
@Configuration
public class SofiaEnvironment {

  public static final String DEV = "dev";
  public static final String PROD = "prod";
  public static final String DEFAULT_DIRECTORY_INDEX_FILE = "index.html";
  public static final String SOFIA_DIRECTORY_NAME = "sofia";
  public static final String LIBS_DIRECTORY_NAME = "libs";
  public static final String SITES_DIRECTORY_NAME = "sites";
  public static final String SOURCES_DIRECTORY_NAME = "sources";
  public static final String IMAGES_FOLDER_NAME = "images";
  public static final String TEXTS_FOLDER_NAME = "texts";
  public static final String DATA_DIRECTORY_NAME = "data";
  public static final String TEMPLATES_DIRECTORY_NAME = "templates";
  public static final String SOFIA_CONFIGURATION_FILENAME = "sofia.yml";
  private static final Logger log = LoggerFactory.getLogger(SofiaEnvironment.class);
  private Path sourcePath;
  private @Autowired SiteManager siteManager;
  private String name;
  @Getter
  @Setter
  private Path basePath;
  @Getter
  @Setter
  private Path sitesPath;
  @Getter
  @Setter
  private Path systemLibraryPath;
  @Getter
  @Setter
  private Path systemDataPath;
  @Getter
  @Setter
  private Path systemTemplatesPath;
  @Getter
  @Setter
  private Path sitesDataPath;
  @Getter
  @Value("${sofia.security.active}")
  private boolean sofiaSecurityActive;

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

  @Bean
  public boolean isDevelopment() {
    return DEV.equals(name);
  }

  public boolean isProduction() {
    return PROD.equals(name);
  }

  public void createHostToSiteData(ConfigurationFileYAMLData configurationFileYAMLData) {
    log.debug("Create host to site data.");
    for (ConfigurationFileYAMLSiteData configurationFileYAMLSiteData : configurationFileYAMLData.getSites()) {
      log.debug("Found data for site: " + configurationFileYAMLSiteData.getName());
      configurationFileYAMLSiteData.getHosts().forEach(hostname -> {
        Host host = new Host(hostname);
        siteManager.add(configurationFileYAMLSiteData, host, isSecurityActive());
      });
    }
  }

  public Path getSourcePath() {
    return sourcePath;
  }

  public void setSourcePath(Path sourcePath) {
    this.sourcePath = sourcePath;
  }

  public boolean isSecurityActive() {
    return sofiaSecurityActive;
  }
}
