package net.cabezudo.sofia.calendar;

import net.cabezudo.sofia.users.UserPreferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@RestController
public class CalendarsController extends SofiaController {

  private final Logger log = LoggerFactory.getLogger(CalendarsController.class);

  @GetMapping("/v1/calendars/weeks")
  public ResponseEntity<?> actual(ServletWebRequest webRequest, Authentication authentication, @RequestParam(defaultValue = "0") int offset) {
    HttpServletRequest request = webRequest.getRequest();

    UserPreferences userPreferences = super.getUserPreferences(request, authentication);

    Locale locale = userPreferences.getLocale();

    Week week = new Week(locale, offset);
    WeekRestResponse calendarRestResponse = new WeekRestResponse(week);
    return ResponseEntity.ok(calendarRestResponse);
  }
}

