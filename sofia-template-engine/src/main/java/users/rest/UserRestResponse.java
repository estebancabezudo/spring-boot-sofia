package users.rest;

import net.cabezudo.sofia.core.rest.SofiaRestResponse;
import net.cabezudo.sofia.users.rest.RestUser;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UserRestResponse extends SofiaRestResponse<RestUser> {
  private final String referer;

  public UserRestResponse(int status, String referer, String message, String username, Collection<GrantedAuthority> authorities) {
    super(status, message);
    this.referer = referer;
  }

  public UserRestResponse(int status, String message, RestUser user) {
    super(status, message, user);
    referer = null;
  }

  public String getReferer() {
    return referer;
  }
}
