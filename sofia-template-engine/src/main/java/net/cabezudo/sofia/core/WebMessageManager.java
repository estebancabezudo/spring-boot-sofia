package net.cabezudo.sofia.core;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WebMessageManager {
  private @Autowired HttpServletRequest request;

  public String getMessage() {
    return (String) request.getSession().getAttribute("message");
  }

  public void setMessage(String message) {
    request.getSession().setAttribute("message", message);
  }

  public void clearMessage() {
    HttpSession session = request.getSession();
    session.removeAttribute("message");
  }
}
