package net.cabezudo.sofia.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfigurationFileYAMLSiteData {
  private final ConfigurationFileYAMLLoginData login = new ConfigurationFileYAMLLoginData();
  private String name;
  private ConfigurationFileYAMLMailData mail;
  private String domainName;
  private List<String> hosts;
  private List<String> permissions;
  private List<String> api;
  private String root;

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

  public String getRoot() {
    return root;
  }

  public ConfigurationFileYAMLMailData getMail() {
    return mail;
  }

  public ConfigurationFileYAMLLoginData getLogin() {
    return login;
  }
}
