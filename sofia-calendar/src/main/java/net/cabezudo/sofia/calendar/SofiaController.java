package net.cabezudo.sofia.calendar;

import net.cabezudo.sofia.sites.Site;
import net.cabezudo.sofia.users.UserPreferences;
import net.cabezudo.sofia.users.UserPreferencesManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class SofiaController {
  private static final Logger log = LoggerFactory.getLogger(SofiaController.class);

  private Locale getLocale(HttpServletRequest request) {
    String header = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
    Site site = (Site) request.getSession().getAttribute("site");
    if (header != null) {
      List<Locale> siteLocales = site.getSiteLocales();
      List<Locale> requestLocaleList = Collections.list(request.getLocales());
      for (Locale requestLocale : requestLocaleList) {
        for (Locale siteLocale : siteLocales) {
          if (requestLocale.equals(siteLocale)) {
            return requestLocale;
          }
        }
      }
    }

    if (site.getDefaultLocale() != null) {
      return site.getDefaultLocale();
    }

    return Locale.ENGLISH;
  }

  protected UserPreferences getUserPreferences(HttpServletRequest request, Authentication authentication) {
    UserPreferences userPreferencesFromSession = (UserPreferences) request.getSession().getAttribute("userPreferences");
    if (userPreferencesFromSession != null) {
      return userPreferencesFromSession;
    }
    if (authentication != null) {
      UserPreferences userPreferencesFromDatabase = UserPreferencesManager.getInstance().get(authentication.getName());
      if (userPreferencesFromDatabase != null) {
        request.getSession().setAttribute("userPreferences", userPreferencesFromDatabase);
        return userPreferencesFromDatabase;
      }
    }
    ZoneOffset zoneOffset = getTimeZone(request);
    Locale locale = getLocale(request);
    UserPreferences userPreferences = new UserPreferences(zoneOffset, locale);
    request.getSession().setAttribute("userPreferences", userPreferences);
    return userPreferences;
  }

  private ZoneOffset getTimeZone(HttpServletRequest request) {
    String timeZoneParameter = request.getParameter("zoneOffset");
    if (isOffset(timeZoneParameter)) {
      int timeZoneOffset = Integer.parseInt(timeZoneParameter);

      int zoneOffset = timeZoneOffset * 60;
      return ZoneOffset.ofTotalSeconds(zoneOffset);
    }
    return ZoneOffset.UTC;
  }

  private boolean isOffset(String timeZoneParameter) {
    if (timeZoneParameter == null) {
      return false;
    }
    if (timeZoneParameter.length() > 5) {
      return false;
    }

    char[] timeZoneParameterChars = timeZoneParameter.toCharArray();
    for (char c : timeZoneParameterChars) {
      if (!Character.isDigit(c) && c != '-' && c != '+') {
        return false;
      }
    }
    return true;
  }
}
