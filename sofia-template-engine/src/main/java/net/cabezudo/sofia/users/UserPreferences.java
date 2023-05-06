package net.cabezudo.sofia.users;

import java.time.ZoneOffset;
import java.util.Locale;

public class UserPreferences {

  private final Locale locale;
  private final ZoneOffset zoneOffset;

  public UserPreferences(ZoneOffset zoneOffset, Locale locale) {
    this.zoneOffset = zoneOffset;
    this.locale = locale;
  }

  public Locale getLocale() {
    return locale;
  }

  public ZoneOffset getZoneOffset() {
    return zoneOffset;
  }
}
