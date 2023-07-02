package net.cabezudo.sofia.security;

import net.cabezudo.sofia.accounts.Account;
import net.cabezudo.sofia.core.SofiaEnvironment;
import net.cabezudo.sofia.core.rest.SofiaController;
import net.cabezudo.sofia.core.rest.SofiaRestResponse;
import net.cabezudo.sofia.users.SofiaUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;

@Controller
public abstract class SofiaAuthorizedController extends SofiaController {
  private static final Logger log = LoggerFactory.getLogger(SofiaAuthorizedController.class);
  private @Autowired SofiaSecurityManager sofiaSecurityManager;
  private @Autowired PermissionManager permissionManager;

  private @Autowired SofiaEnvironment sofiaEnvironment;

  public SofiaAuthorizedController(HttpServletRequest request) {
    super(request);
  }

  protected ResponseEntity<SofiaRestResponse<?>> checkPermissionFor(Account account, String group) {
    SofiaUser user = sofiaSecurityManager.getLoggedUser();
    if (user == null) {
      log.debug("The user is null");
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new SofiaRestResponse<>(SofiaRestResponse.ERROR, "Not logged"));
    }
    if (!permissionManager.hasPermission(account.getSite(), account, user, group, sofiaEnvironment.isSecurityActive())) {
      log.debug("The user(" + user.getUsername() + ") HAS NOT not permission(group=" + group + ") for th account (" + account.getName() + "(" + account.getId() + "))");
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new SofiaRestResponse<>(SofiaRestResponse.ERROR, "The user(" + user + ") has not permission(group=" + group + ") for th account (" + account + ")"));
    }
    return null;
  }
}
