package net.cabezudo.sofia.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import net.cabezudo.sofia.core.SofiaEnvironment;
import net.cabezudo.sofia.core.SofiaResourceResolver;
import net.cabezudo.sofia.creator.ContentManager;
import net.cabezudo.sofia.security.Permission;
import net.cabezudo.sofia.security.PermissionManager;
import net.cabezudo.sofia.sites.Site;
import net.cabezudo.sofia.sites.SiteNotFoundException;
import net.cabezudo.sofia.sites.service.PathManager;
import net.cabezudo.sofia.sites.service.SiteManager;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
public class TemplateEngineConfiguration implements WebMvcConfigurer {
  private static final Logger log = LoggerFactory.getLogger(TemplateEngineConfiguration.class);
  private @Autowired SofiaEnvironment sofiaEnvironment;
  private @Autowired SiteManager siteManager;
  private @Autowired PathManager pathManager;
  private @Autowired PermissionManager permissionManager;
  private @Autowired ContentManager contentManager;


  @EventListener(ApplicationReadyEvent.class)
  public void loadData() throws ConfigurationException {
    loadConfiguration();
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry
        .addResourceHandler("/images/**", "/fonts/**")
        .setCacheControl(CacheControl.maxAge(20, TimeUnit.DAYS)
            .noTransform()
            .mustRevalidate())
        .resourceChain(sofiaEnvironment.isProduction()).addResolver(new SofiaResourceResolver(siteManager, pathManager));
    registry
        .addResourceHandler("/**")
        .resourceChain(false).addResolver(new SofiaResourceResolver(siteManager, pathManager));
  }

  private void loadConfiguration() throws ConfigurationException {
    log.debug("Load configuration for template engine environment.");
    try {
      loadDataFromYAMLFile();
    } catch (SiteNotFoundException e) {
      throw new ConfigurationException(e);
    }
    setSystemBasePath();
    log.debug("Sites path: " + sofiaEnvironment.getSitesPath());
    setSourcesPaths();
    sofiaEnvironment.setSystemLibraryPath(sofiaEnvironment.getBasePath().resolve(SofiaEnvironment.LIBS_DIRECTORY_NAME));
    if (Files.exists(sofiaEnvironment.getSystemLibraryPath()) && !Files.isDirectory(sofiaEnvironment.getSystemLibraryPath())) {
      throw new ConfigurationException("The system library path is is not a directory: " + sofiaEnvironment.getSourcePaths());
    }
    log.debug("System library path: " + sofiaEnvironment.getSystemLibraryPath());
    sofiaEnvironment.setSystemDataPath(sofiaEnvironment.getBasePath().resolve(SofiaEnvironment.DATA_DIRECTORY_NAME));
    sofiaEnvironment.setSystemTemplatesPath(sofiaEnvironment.getSystemDataPath().resolve(SofiaEnvironment.TEMPLATES_DIRECTORY_NAME));
    sofiaEnvironment.setSitesDataPath(sofiaEnvironment.getSystemDataPath().resolve(SofiaEnvironment.SITES_DIRECTORY_NAME));
  }

  private void loadDataFromYAMLFile() throws SiteNotFoundException {
    log.debug("Load data from configuration file sofia.yml.");
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    ClassLoader classLoader = this.getClass().getClassLoader();
    URL sofiaConfigurationURL = classLoader.getResource(SofiaEnvironment.SOFIA_CONFIGURATION_FILENAME);
    if (sofiaConfigurationURL == null) {
      log.debug("Configuration file sofia.yml NOT FOUND in resources path.");
      sofiaEnvironment.setConfigurationFileYAMLData(new ConfigurationFileYAMLData());
    } else {
      File sofiaConfigurationFile = new File(sofiaConfigurationURL.getFile());
      try {
        sofiaEnvironment.setConfigurationFileYAMLData(mapper.readValue(sofiaConfigurationFile, ConfigurationFileYAMLData.class));
        sofiaEnvironment.createHostToSiteData();
        addPermissions(sofiaEnvironment.getConfigurationFileYAMLData());
      } catch (IOException e) {
        log.error("invalid configuration: " + e.getMessage());
      }
    }
  }

  public void addPermissions(ConfigurationFileYAMLData configurationFileYAMLData) {
    log.debug("Create permissions with data in configuration file.");
    List<ConfigurationFileYAMLSiteData> sites = configurationFileYAMLData.getSites();
    for (ConfigurationFileYAMLSiteData siteData : sites) {
      Site site;
      try {
        site = siteManager.getByName(siteData.getName());
      } catch (SiteNotFoundException e) {
        continue;
      }
      log.debug("Site: " + site.getName());

      List<String> apis = siteData.getAPI();
      for (String api : apis) {
        contentManager.add(site, api);
        String permissionString = "all:all:grant:" + api + ("/".equals(api) ? "**" : "/**");
        Permission permission = new Permission(permissionString);
        permissionManager.add(site, permission);
        log.debug("Add " + permissionString + " to site " + site.getName());
      }

      List<String> permissions = siteData.getPermissions();
      for (String permissionData : permissions) {
        Permission permission = new Permission(permissionData);
        permissionManager.add(site, permission);
        log.debug("Add " + permissionData + " to site " + site.getName());
      }
    }
  }


  private void setSystemBasePath() throws ConfigurationException {
    String stringBasePathFromConfigurationFile = sofiaEnvironment.getConfigurationFileYAMLData().getBasePath();
    String stringBasePath;
    ClassLoader classLoader = this.getClass().getClassLoader();
    if (stringBasePathFromConfigurationFile == null) {
      URL url = classLoader.getResource(SofiaEnvironment.SOFIA_DIRECTORY_NAME);
      log.debug("URL: " + url);
      if (url == null) {
        throw new ConfigurationException("Cant find the system path. Maybe you must define a basePath property in a sofia.yml file.");
      }
      stringBasePath = url.getFile();
    } else {
      stringBasePath = stringBasePathFromConfigurationFile;
    }
    sofiaEnvironment.setBasePath(Paths.get(stringBasePath));
    if (!Files.exists(sofiaEnvironment.getBasePath())) {
      log.warn("Base path doesn't exist: " + sofiaEnvironment.getBasePath());
      log.info("Trying with resources");
      URL url = classLoader.getResource(SofiaEnvironment.SOFIA_DIRECTORY_NAME);
      if (url == null) {
        throw new ConfigurationException("Resource for system base path not found: " + SofiaEnvironment.SOFIA_DIRECTORY_NAME);
      }

      String urlFile = url.getFile();
      sofiaEnvironment.setBasePath(Paths.get(urlFile));
    }
    if (!Files.isDirectory(sofiaEnvironment.getBasePath())) {
      throw new ConfigurationException("Base path is not a directory: " + sofiaEnvironment.getBasePath());
    }
    log.debug("Base path: " + sofiaEnvironment.getBasePath());

    try {
      log.debug("Sites final code: " + sofiaEnvironment.getBasePath());
      sofiaEnvironment.setSitesPath(sofiaEnvironment.getBasePath().resolve(SofiaEnvironment.SITES_DIRECTORY_NAME));
      if (Files.exists(sofiaEnvironment.getSitesPath())) {
        FileUtils.forceDelete(sofiaEnvironment.getSitesPath().toFile());
      }
      Files.createDirectories(sofiaEnvironment.getSitesPath());
    } catch (IOException e) {
      throw new ConfigurationException(e);
    }

    if (!Files.isDirectory(sofiaEnvironment.getSitesPath())) {
      throw new ConfigurationException("Base path is not a directory: " + sofiaEnvironment.getSitesPath());
    }
  }

  private void setSourcesPaths() throws ConfigurationException {
    String[] sourcesPathsFromData = sofiaEnvironment.getConfigurationFileYAMLData().getSourcePaths();
    if (sourcesPathsFromData == null || sourcesPathsFromData.length == 0) {
      sofiaEnvironment.getSourcePaths().add(sofiaEnvironment.getBasePath().resolve(SofiaEnvironment.SOURCES_DIRECTORY_NAME));
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
        sofiaEnvironment.getSourcePaths().add(path);
      }
    }
  }

}
