package net.cabezudo.sofia.web.client;

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
  private @Autowired WebClientManager webClientManager;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    WebClientData webClientData = (WebClientData) request.getSession().getAttribute(WebClientData.OBJECT_NAME_IN_SESSION);

    if (webClientData == null) {
      WebClientData newWebClientData = new WebClientData(null, null); // TODO Get the language from the web client nor from the site preferences
      request.getSession().setAttribute(WebClientData.OBJECT_NAME_IN_SESSION, newWebClientData);
    }
    filterChain.doFilter(request, response);
  }
}