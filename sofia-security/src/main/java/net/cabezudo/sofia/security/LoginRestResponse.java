package net.cabezudo.sofia.security;

import com.fasterxml.jackson.annotation.JsonInclude;

public class LoginRestResponse {
  private final String status;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private final String redirection;

  public LoginRestResponse(String status, String redirection) {
    this.status = status;
    this.redirection = redirection;
  }

  public LoginRestResponse(String status) {
    this(status, null);
  }

  public String getStatus() {
    return status;
  }

  public String getRedirection() {
    return redirection;
  }
}
