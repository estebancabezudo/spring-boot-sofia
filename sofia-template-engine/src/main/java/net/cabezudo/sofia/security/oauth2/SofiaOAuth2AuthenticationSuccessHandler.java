package net.cabezudo.sofia.security.oauth2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.cabezudo.sofia.security.LoginRestResponse;
import net.cabezudo.sofia.security.SofiaSecurityConfig;
import net.cabezudo.sofia.sites.Site;
import net.cabezudo.sofia.sites.service.SiteManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import java.io.IOException;
import java.io.PrintWriter;

public class SofiaOAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private static final Logger log = LoggerFactory.getLogger(SofiaOAuth2AuthenticationSuccessHandler.class);
  private final SiteManager siteManager;
  private RequestCache requestCache = new HttpSessionRequestCache();

  public SofiaOAuth2AuthenticationSuccessHandler(SiteManager siteManager) {
    this.siteManager = siteManager;
  }

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
      Site site = siteManager.getSite(request);
      response.sendRedirect(site.getLoginSuccessURL());
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
