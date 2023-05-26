package net.cabezudo.sofia.web.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
public class WebClientDataManager {
  private @Autowired HttpServletRequest request;

  public WebClientData getFromSession() {
    HttpSession session = request.getSession();
    return (WebClientData) session.getAttribute(WebClientData.OBJECT_NAME_IN_SESSION);
  }
}
