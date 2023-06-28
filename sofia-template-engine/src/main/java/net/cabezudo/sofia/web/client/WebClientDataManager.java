package net.cabezudo.sofia.web.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
public class WebClientDataManager {
  public static final String WEB_CLIENT_DATA_NAME_IN_SESSION = "ClientData";
  private static final Logger log = LoggerFactory.getLogger(WebClientDataManager.class);

  public WebClientData getFromSession(HttpServletRequest request) {
    HttpSession session = request.getSession();
    WebClientData webClientData = (WebClientData) session.getAttribute(WEB_CLIENT_DATA_NAME_IN_SESSION);
    return webClientData;
  }

  public void set(HttpServletRequest request, WebClientData webClientData) {
    HttpSession session = request.getSession();
    session.setAttribute(WEB_CLIENT_DATA_NAME_IN_SESSION, webClientData);
  }
}
