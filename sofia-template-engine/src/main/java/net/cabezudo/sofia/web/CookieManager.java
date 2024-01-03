package net.cabezudo.sofia.web;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

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
