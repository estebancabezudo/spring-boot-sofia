package net.cabezudo.sofia.config;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.01.00, 2022.09.02
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfigurationFileYAMLData {

  private String basePath;
  private boolean securityActive;
  private String[] sourcePaths;
  private List<ConfigurationFileYAMLSiteData> sites;

  public ConfigurationFileYAMLData() {
    // Without a default constructor, Jackson will throw an exception
  }

  public ConfigurationFileYAMLData(String basePath, List<ConfigurationFileYAMLSiteData> sites) {
    this.basePath = basePath;
    this.sites = sites;
  }

  public boolean isSecurityActive() {
    return securityActive;
  }

  public String[] getSourcePaths() {
    return sourcePaths;
  }

  public String getBasePath() {
    return basePath;
  }

  public List<ConfigurationFileYAMLSiteData> getSites() {
    return sites;
  }

}
