package net.cabezudo.sofia.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfigurationFileYAMLSiteData {
  private String name;
  private List<String> hosts;
  private List<String> permissions;
  private List<String> api;

  public ConfigurationFileYAMLSiteData() {
    // Without a default constructor, Jackson will throw an exception
  }

  public List<String> getPermissions() {
    if (permissions == null) {
      permissions = new ArrayList<>();
    }
    return permissions;
  }

  public List<String> getAPI() {
    if (api == null) {
      api = new ArrayList<>();
    }
    return api;
  }

  public String getName() {
    return name;
  }

  public List<String> getHosts() {
    return hosts;
  }
}
