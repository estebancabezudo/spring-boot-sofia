package net.cabezudo.sofia.users.rest;

import net.cabezudo.sofia.core.rest.SofiaRestResponse;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class SofiaUserRestResponse extends SofiaRestResponse<RestSofiaUser> {
  private final String referer;

  public SofiaUserRestResponse(int status, String referer, String message, String username, Collection<GrantedAuthority> authorities) {
    super(status, message);
    this.referer = referer;
  }

  public SofiaUserRestResponse(int status, String message, RestSofiaUser user) {
    super(status, message, user);
    referer = null;
  }

  public String getReferer() {
    return referer;
  }
}
