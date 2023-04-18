package net.cabezudo.sofia.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfigurationFileYAMLSiteData {
  private String name;
  private List<String> hosts;
  private List<String> permissions;
  private List<String> apis;

  public ConfigurationFileYAMLSiteData() {
    // Without a default constructor, Jackson will throw an exception
  }

  public List<String> getPermissions() {
    if (permissions == null) {
      permissions = new ArrayList<>();
    }
    return permissions;
  }

  public List<String> getAPIs() {
    if (apis == null) {
      apis = new ArrayList<>();
    }
    return apis;
  }

  public String getName() {
    return name;
  }

  public List<String> getHosts() {
    return hosts;
  }
}
