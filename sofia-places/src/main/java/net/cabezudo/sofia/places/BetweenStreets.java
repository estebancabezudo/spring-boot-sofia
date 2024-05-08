package net.cabezudo.sofia.places;

public class BetweenStreets {
  private final Street first;
  private final Street second;

  public BetweenStreets(Street first, Street second) {
    this.first = first;
    this.second = second;
  }

  public Street getFirst() {
    return first;
  }

  public Street getSecond() {
    return second;
  }
}
