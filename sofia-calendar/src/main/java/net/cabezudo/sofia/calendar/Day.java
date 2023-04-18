package net.cabezudo.sofia.calendar;

public class Day {
  private final String name;
  private final String letterName;
  private final int dayOfMonth;
  public Day(String name, String letterName, int dayOfMonth) {
    this.name = name;
    this.letterName = letterName;
    this.dayOfMonth = dayOfMonth;
  }

  public String getLetterName() {
    return letterName;
  }

  public int getDayOfMonth() {
    return dayOfMonth;
  }

  public String getName() {
    return name;
  }
}
