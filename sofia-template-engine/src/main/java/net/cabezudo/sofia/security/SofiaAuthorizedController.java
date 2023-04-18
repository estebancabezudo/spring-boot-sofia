package net.cabezudo.sofia.security;

import net.cabezudo.sofia.accounts.Account;
import net.cabezudo.sofia.accounts.AccountManager;
import net.cabezudo.sofia.accounts.Accounts;
import net.cabezudo.sofia.config.SofiaEnvironment;
import net.cabezudo.sofia.core.SofiaRuntimeException;
import net.cabezudo.sofia.core.rest.RestErrorResponse;
import net.cabezudo.sofia.core.rest.SofiaController;
import net.cabezudo.sofia.users.SofiaUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;

@Controller
public abstract class SofiaAuthorizedController extends SofiaController {

  private final Logger log = LoggerFactory.getLogger(SofiaAuthorizedController.class);
  @Autowired
  AccountManager accountManager;
  @Autowired
  PermissionManager permissionManager;
  @Autowired
  private SofiaEnvironment sofiaEnvironment;

  public SofiaAuthorizedController(HttpServletRequest request) {
    super(request);
  }

  protected SofiaUser getSofiaUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication instanceof UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) {
      return (SofiaUser) usernamePasswordAuthenticationToken.getPrincipal();
    }
    return null;
  }

  protected ResponseEntity checkPermissionFor(Account account, String group) {
    SofiaUser user = getSofiaUser();
    if (user == null && sofiaEnvironment.isSecurityActive()) {
      log.debug("The user is null and the security is active");
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(RestErrorResponse.UNAUTHORIZED);
    }
    if (!permissionManager.hasPermission(account, user, group)) {
      log.debug("The user(" + user + ") has not permission(group=" + group + ") for th account (" + account + ")");
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(RestErrorResponse.FORBIDDEN);
    }
    return null;
  }

  protected Account getAccount() {
    if (sofiaEnvironment.isSecurityNotActive() && sofiaEnvironment.isDevelopment()) {
      return new Account(1);
    }

    SofiaUser user = getSofiaUser();
    if (user == null) {
      return null;
    }
    Accounts accounts = accountManager.getAll(user);
    Account accountFromSession = (Account) super.getRequest().getSession().getAttribute("account");
    if (accountFromSession == null) {
      Account account = accounts.getFirst();
      if (account == null) {
        throw new SofiaRuntimeException("null account in account list from manager. Incorrect data from database. A user must have an account.");
      }
      super.getRequest().getSession().setAttribute("account", account);
      return account;
    } else {
      if (accounts.contains(accountFromSession)) {
        return accountFromSession;
      } else {
        return accounts.getFirst();
      }
    }
  }
}
