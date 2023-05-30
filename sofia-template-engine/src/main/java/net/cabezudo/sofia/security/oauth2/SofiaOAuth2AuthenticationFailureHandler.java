package net.cabezudo.sofia.security.oauth2;

import net.cabezudo.sofia.security.SofiaSecurityConfig;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SofiaOAuth2AuthenticationFailureHandler implements AuthenticationFailureHandler {

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
    response.sendRedirect(SofiaSecurityConfig.DEFAULT_LOGIN_PAGE);
  }
}
