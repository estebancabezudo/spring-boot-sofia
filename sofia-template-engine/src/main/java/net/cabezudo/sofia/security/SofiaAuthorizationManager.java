package net.cabezudo.sofia.security;

import net.cabezudo.sofia.accounts.service.Account;
import net.cabezudo.sofia.accounts.service.AccountManager;
import net.cabezudo.sofia.core.SofiaEnvironment;
import net.cabezudo.sofia.core.SofiaRuntimeException;
import net.cabezudo.sofia.sites.Site;
import net.cabezudo.sofia.sites.SiteManager;
import net.cabezudo.sofia.sites.SiteNotFoundException;
import net.cabezudo.sofia.users.service.SofiaUser;
import net.cabezudo.sofia.web.client.WebClientDataManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.function.Supplier;

@Service
public class SofiaAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
  private static final Logger log = LoggerFactory.getLogger(SofiaAuthorizationManager.class);
  private @Autowired PermissionManager permissionManager;
  private @Autowired SiteManager siteManager;
  private @Autowired SofiaSecurityManager sofiaSecurityManager;
  private @Autowired WebClientDataManager webClientDataManager;
  private @Autowired SofiaEnvironment sofiaEnvironment;
  private @Autowired AccountManager accountManager;

  @Override
  public AuthorizationDecision check(Supplier<Authentication> supplier, RequestAuthorizationContext context) {
    if (supplier == null) {
      return new AuthorizationDecision(false);
    }

    HttpServletRequest request = context.getRequest();

    String requestURI = request.getRequestURI();
    String servletPath = request.getServletPath();

    // The following lines are to avoid the dummy request provided by Spring that raise a UnsupportedOperationException when call /error
    if ("/error".equals(servletPath) || SofiaSecurityConfig.DEFAULT_LOGIN_PAGE.equals(servletPath)) { // TODO Improve this
      return new AuthorizationDecision(true);
    }
    Site site;
    try {
      site = siteManager.getByHostname(request.getServerName());
    } catch (SiteNotFoundException e) {
      throw new SofiaRuntimeException(e);
    }

    if (sofiaEnvironment.isSecurityActive()) {
      SofiaUser user = sofiaSecurityManager.getLoggedUser();
      Account account = accountManager.getAccount(request);
      log.debug("Check authorization permissions for " + requestURI + "  using " + (user == null ? "NOT LOGGED" : user.getUsername()) + " on " + site.getName() + (account == null ? null : account.getName() + " in the account " + "(" + account.getId() + ")"));
      if (site != null && permissionManager.hasPermission(user, site, account, requestURI)) {
        return new AuthorizationDecision(true);
      }
      return new AuthorizationDecision(false);
    } else {
      return new AuthorizationDecision(true);
    }
  }
}
