package net.cabezudo.sofia.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfigurationFileYAMLLoginData {
  @JsonProperty("successURL")
  private String successURL = "/";

  public String getSuccessURL() {
    return successURL;
  }

  public void setSuccessURL(String successURL) {
    this.successURL = successURL;
  }
}
