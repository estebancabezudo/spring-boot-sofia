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
  private String sourcePath;
  private List<ConfigurationFileYAMLSiteData> sites;

  public ConfigurationFileYAMLData() {
    // Without a default constructor, Jackson will throw an exception
  }

  public ConfigurationFileYAMLData(String basePath, String sourcePath, List<ConfigurationFileYAMLSiteData> sites) {
    this.basePath = basePath;
    this.sourcePath = sourcePath;
    this.sites = sites;
  }

  public String getSourcePath() {
    return sourcePath;
  }

  public String getBasePath() {
    return basePath;
  }

  public List<ConfigurationFileYAMLSiteData> getSites() {
    return sites;
  }

}
