package net.cabezudo.sofia.web.client;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.cabezudo.sofia.web.user.WebUserData;
import net.cabezudo.sofia.web.user.WebUserDataManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ClientConfigurationFilter extends OncePerRequestFilter {
  private static final Logger log = LoggerFactory.getLogger(ClientConfigurationFilter.class);
  private @Autowired WebClientDataManager webClientDataManager;
  private @Autowired WebUserDataManager webUserDataManager;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    WebClientData webClientDataFromSession = webClientDataManager.getFromCookie(request);
    if (webClientDataFromSession == null) {
      WebClientData newClientDataToCreate = new WebClientData(null, null, null, null); // The language is defined when the first text file is requested.
      WebClientData newWebClientData = webClientDataManager.create(newClientDataToCreate);
      log.debug("The web client data is null. Create " + newWebClientData);
      webClientDataManager.setCookie(response, newWebClientData);
      request.setAttribute(WebClientDataManager.WEB_CLIENT_COOKIE_DATA_NAME, newWebClientData);
    } else {
      log.debug("Found client data " + webClientDataFromSession);
    }

    WebUserData webUserDataFromSession = webUserDataManager.getFromSession(request);
    if (webUserDataFromSession == null) {
      WebUserData webUserData = new WebUserData(null, null, null);
      log.debug("The web user data is null. Create " + webUserData);
      webUserDataManager.set(request, webUserData);
    }

    filterChain.doFilter(request, response);
  }
}