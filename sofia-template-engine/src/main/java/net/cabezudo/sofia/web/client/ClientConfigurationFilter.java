package net.cabezudo.sofia.web.client;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ClientConfigurationFilter extends OncePerRequestFilter {
  private static final Logger log = LoggerFactory.getLogger(ClientConfigurationFilter.class);
  private @Autowired WebClientDataManager webClientDataManager;

  @Override
  protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {

    WebClientData webClientDataFromSession = webClientDataManager.getFromSession(request);
    if (webClientDataFromSession == null) {
      WebClientData newWebClientData = new WebClientData(null, null); // The language is defined when the first text is requested.
      log.debug("The client data is null. Create " + newWebClientData);

      webClientDataManager.set(request, newWebClientData);
    } else {
      log.debug("Found client data " + webClientDataFromSession);
    }
    filterChain.doFilter(request, response);
  }
}