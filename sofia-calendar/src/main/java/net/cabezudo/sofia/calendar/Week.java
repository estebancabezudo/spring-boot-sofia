package net.cabezudo.sofia.calendar;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Week {
  private final List<Day> days;

  public Week(Locale locale, int offset) {
    days = new ArrayList<>();
    LocalDate now = LocalDate.now();
    LocalDate dateWithOffset = now.plusWeeks(offset);
    LocalDate firstDayOfWeek = dateWithOffset.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    for (int i = 0; i < 7; i++) {
      LocalDate localDateToShow = firstDayOfWeek.plusDays(i);
      DayOfWeek dayOfWeek = DayOfWeek.from(localDateToShow);
      String name = dayOfWeek.getDisplayName(TextStyle.FULL, locale);
      String letterName = dayOfWeek.getDisplayName(TextStyle.NARROW, locale);
      int dayOfMonth = localDateToShow.getDayOfMonth();
      Day day = new Day(name, letterName, dayOfMonth);
      days.add(day);
    }
  }

  public List<Day> getDays() {
    return days;
  }
}
