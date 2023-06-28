package net.cabezudo.sofia.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfigurationFileYAMLMailData {
  @JsonProperty(required = true)
  private String replyAddress;

  public String getReplyAddress() {
    return replyAddress;
  }
}
