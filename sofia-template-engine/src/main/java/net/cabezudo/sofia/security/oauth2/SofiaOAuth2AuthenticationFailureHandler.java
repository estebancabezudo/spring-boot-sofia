package net.cabezudo.sofia.security.oauth2;

import jakarta.servlet.ServletException;
import net.cabezudo.sofia.security.SofiaSecurityConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

public class SofiaOAuth2AuthenticationFailureHandler implements AuthenticationFailureHandler {
  private static final Logger log = LoggerFactory.getLogger(SofiaOAuth2AuthenticationFailureHandler.class);

  @Override
  public void onAuthenticationFailure(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
    log.debug("OAuth failure");
    response.sendRedirect(SofiaSecurityConfig.DEFAULT_LOGIN_PAGE);
  }
}
