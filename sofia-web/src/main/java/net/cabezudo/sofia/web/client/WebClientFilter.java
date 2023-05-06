package net.cabezudo.sofia.web.client;

import net.cabezudo.sofia.web.client.rest.RestWebClient;
import net.cabezudo.sofia.web.client.service.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Component
public class WebClientFilter implements Filter {
  private static final Logger log = LoggerFactory.getLogger(WebClientFilter.class);

  private @Autowired WebClientManager webClientManager;

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;

    Cookie cookie = null;
    if (request.getCookies() != null) {
      cookie = Arrays.stream(request.getCookies())
          .filter(c -> c.getName().equals(RestWebClient.COOKIE_ID_NAME))
          .findFirst()
          .orElse(null);
    }

    if (cookie == null) {
      log.debug("Cookie " + RestWebClient.COOKIE_ID_NAME + " not found.");
      WebClient webClient = webClientManager.create();
      Cookie newCookie = new Cookie(RestWebClient.COOKIE_ID_NAME, Long.toString(webClient.getId()));
      newCookie.setMaxAge(39 * 24 * 60 * 60);
      newCookie.setSecure(true);
      newCookie.setHttpOnly(true);
      newCookie.setPath("/");
      response.addCookie(newCookie);
      request.getSession().setAttribute(WebClient.OBJECT_NAME_IN_SESSION, webClient);
    } else {
      Object webClientFromSession = request.getSession().getAttribute(WebClient.OBJECT_NAME_IN_SESSION);
      if (webClientFromSession == null) {
        long id = Long.parseLong(cookie.getValue());
        WebClient webClientFromManager = webClientManager.loadWebClientBySessionId(id);
        WebClient webClient;
        if (webClientFromManager == null) {
          webClient = webClientManager.create();
        } else {
          webClient = webClientFromManager;
        }
        request.getSession().setAttribute(WebClient.OBJECT_NAME_IN_SESSION, webClient);
      }
    }
    chain.doFilter(request, response);
  }
}