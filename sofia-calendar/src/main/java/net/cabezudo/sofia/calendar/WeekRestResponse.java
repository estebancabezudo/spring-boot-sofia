package net.cabezudo.sofia.calendar;

public class WeekRestResponse {
  private final Week week;

  public WeekRestResponse(Week week) {
    this.week = week;
  }

  public Week getWeek() {
    return week;
  }
}
