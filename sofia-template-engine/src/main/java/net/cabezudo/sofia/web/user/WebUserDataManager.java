package net.cabezudo.sofia.web.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class WebUserDataManager {
  public static final String WEB_USER_DATA_NAME_IN_SESSION = "UserData";
  private static final Logger log = LoggerFactory.getLogger(WebUserDataManager.class);

  public WebUserData getFromSession(HttpServletRequest request) {
    HttpSession session = request.getSession();
    WebUserData webUserData = (WebUserData) session.getAttribute(WEB_USER_DATA_NAME_IN_SESSION);
    log.debug("Get user data from session: " + webUserData);
    return webUserData;
  }

  public void set(HttpServletRequest request, WebUserData webUserData) {
    HttpSession session = request.getSession();
    session.setAttribute(WEB_USER_DATA_NAME_IN_SESSION, webUserData);
  }
}
