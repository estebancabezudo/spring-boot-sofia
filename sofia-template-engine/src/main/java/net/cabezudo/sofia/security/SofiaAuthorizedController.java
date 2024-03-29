package net.cabezudo.sofia.security;

import net.cabezudo.sofia.accounts.service.Account;
import net.cabezudo.sofia.core.SofiaEnvironment;
import net.cabezudo.sofia.core.rest.SofiaController;
import net.cabezudo.sofia.core.rest.SofiaRestResponse;
import net.cabezudo.sofia.users.service.SofiaUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public abstract class SofiaAuthorizedController extends SofiaController {
  private static final Logger log = LoggerFactory.getLogger(SofiaAuthorizedController.class);
  private @Autowired SofiaSecurityManager sofiaSecurityManager;
  private @Autowired PermissionManager permissionManager;

  private @Autowired SofiaEnvironment sofiaEnvironment;

  protected ResponseEntity<SofiaRestResponse<?>> checkPermissionFor(Account account, String... groups) {
    SofiaUser user = sofiaSecurityManager.getLoggedUser();
    if (user == null) {
      log.debug("The user is null");
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new SofiaRestResponse<>(SofiaRestResponse.ERROR, "Not logged"));
    }
    if (permissionManager.hasPermission(account.getSite(), account, user, sofiaEnvironment.isSecurityActive(), groups)) {
      return null;
    }
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new SofiaRestResponse<>(SofiaRestResponse.ERROR, "The user(" + user + ") has not permission(" + groups + ") for th account (" + account + ")"));
  }

  protected ResponseEntity<SofiaRestResponse<?>> isLogged() {
    SofiaUser user = sofiaSecurityManager.getLoggedUser();
    if (user == null) {
      log.debug("Is not logged");
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new SofiaRestResponse<>(SofiaRestResponse.ERROR, "Not logged"));
    }
    return null;
  }
}
