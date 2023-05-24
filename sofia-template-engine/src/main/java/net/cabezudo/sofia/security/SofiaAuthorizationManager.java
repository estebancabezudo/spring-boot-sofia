package net.cabezudo.sofia.security;

import net.cabezudo.sofia.sites.Site;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import javax.servlet.http.HttpServletRequest;
import java.util.function.Supplier;

public class SofiaAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
  private static final SofiaAuthorizationManager INSTANCE = new SofiaAuthorizationManager();
  private static final Logger log = LoggerFactory.getLogger(SofiaAuthorizationManager.class);

  public static SofiaAuthorizationManager getInstance() {
    return INSTANCE;
  }

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

    Authentication authentication = supplier.get();
    if (site != null && PermissionManagerImplementation.getInstance().authorize(site, requestURI, authentication.getName(), authentication.getAuthorities())) {
      return new AuthorizationDecision(true);
    }

    return new AuthorizationDecision(false);
  }
}
