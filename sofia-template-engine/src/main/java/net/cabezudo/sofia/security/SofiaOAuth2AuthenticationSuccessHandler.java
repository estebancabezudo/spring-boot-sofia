package net.cabezudo.sofia.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class SofiaOAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private static final Logger log = LoggerFactory.getLogger(SofiaOAuth2AuthenticationSuccessHandler.class);
  private RequestCache requestCache = new HttpSessionRequestCache();

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                      Authentication authentication) throws IOException {

    log.debug("Running the handler for successful oauth2");

    PrintWriter out = response.getWriter();
    if (!(authentication instanceof OAuth2AuthenticationToken)) {
      response.sendRedirect(SofiaSecurityConfig.DEFAULT_LOGIN_PAGE);
      return;
    }

    SavedRequest savedRequest = this.requestCache.getRequest(request, response);
    if (savedRequest == null) {
      response.sendRedirect(SofiaSecurityConfig.DEFAULT_LOGIN_SUCCESS_URL);
      return;
    }
    clearAuthenticationAttributes(request);
    response.sendRedirect(savedRequest.getRedirectUrl());
  }

  private void write(PrintWriter out, LoginRestResponse response) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    String jsonResponse = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
    out.println(jsonResponse);
    out.flush();
  }

  public void setRequestCache(RequestCache requestCache) {
    this.requestCache = requestCache;
  }
}
