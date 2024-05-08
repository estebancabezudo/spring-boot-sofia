package net.cabezudo.sofia.security;

import net.cabezudo.sofia.accounts.service.Account;
import net.cabezudo.sofia.accounts.service.AccountManager;
import net.cabezudo.sofia.core.SofiaEnvironment;
import net.cabezudo.sofia.core.rest.BadRequestException;
import net.cabezudo.sofia.core.rest.SofiaController;
import net.cabezudo.sofia.core.rest.SofiaRestResponse;
import net.cabezudo.sofia.users.service.SofiaUser;
import net.cabezudo.sofia.utils.StringUtils;
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
  private @Autowired AccountManager accountManager;
  private SofiaUser user;

  protected ResponseEntity<SofiaRestResponse<?>> checkPermissionFor(Account account, String... groups) {
    log.debug("Check permissions for " + account + ", " + StringUtils.toString(groups));
    if (getLoggedUser() == null) {
      log.debug("The user is null");
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new SofiaRestResponse<>(SofiaRestResponse.ERROR, "Not logged"));
    }
    if (permissionManager.hasPermission(account.getSite(), account, user, sofiaEnvironment.isSecurityActive(), groups)) {
      log.debug("The user user as permissions");
      return null;
    }
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new SofiaRestResponse<>(SofiaRestResponse.ERROR, "The user(" + user + ") has not permission(" + groups + ") for th account (" + account + ")"));
  }

  protected SofiaUser getLoggedUser() {
    if (user == null) {
      user = sofiaSecurityManager.getLoggedUser();
    }
    return user;
  }

  protected boolean userOwnsTheAccount() throws BadRequestException {
    boolean userOwnsTheAccount = accountManager.ownsTheAccount(user, getAccount());
    log.debug("The user " + user + (userOwnsTheAccount ? "" : " not") + " owns the account " + getAccount());
    return userOwnsTheAccount;
  }

  protected ResponseEntity<SofiaRestResponse<?>> notLoggedResponse() {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new SofiaRestResponse<>(SofiaRestResponse.ERROR, "Not logged"));
  }
}
