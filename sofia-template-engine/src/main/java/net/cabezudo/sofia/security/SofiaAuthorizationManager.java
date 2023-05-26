package net.cabezudo.sofia.security;

import net.cabezudo.sofia.accounts.Account;
import net.cabezudo.sofia.sites.Site;
import net.cabezudo.sofia.users.SofiaUser;
import net.cabezudo.sofia.users.service.UserManager;
import net.cabezudo.sofia.web.client.WebClientData;
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
  private @Autowired UserManager userManager;
  private @Autowired SofiaSecurityManager sofiaSecurityManager;
  private @Autowired WebClientDataManager webClientDataManager;

  @Override
  public AuthorizationDecision check(Supplier<Authentication> supplier, RequestAuthorizationContext context) {
    log.debug("SofiaAuthorizationManager");

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
    Site site = (Site) request.getSession().getAttribute("site");
    SofiaUser user = sofiaSecurityManager.getLoggedUser();
    WebClientData webClientData = webClientDataManager.getFromSession();
    Account account = webClientData.getAccount();
    if (site != null && permissionManager.hasPermission(user, site, account, requestURI)) {
      return new AuthorizationDecision(true);
    }

    return new AuthorizationDecision(false);
  }
}
