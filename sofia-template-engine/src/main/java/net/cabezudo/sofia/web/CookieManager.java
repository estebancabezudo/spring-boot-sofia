package net.cabezudo.sofia.web;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Component
public class CookieManager {
  public Integer getIntegerCookie(HttpServletRequest request, String name) {
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals(name)) {
          String value = cookie.getValue();
          if (value == null) {
            return null;
          }
          if (value.matches("-?\\d+")) {
            return Integer.parseInt(value);
          } else {
            return null;
          }
        }
      }
    }
    return null;
  }
}
