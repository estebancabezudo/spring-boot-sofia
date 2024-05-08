package net.cabezudo.sofia.places.rest;

public class RestBetweenStreets {
  private final RestStreet firstStreet;
  private final RestStreet secondStreet;

  public RestBetweenStreets(RestStreet firstStreet, RestStreet secondStreet) {
    this.firstStreet = firstStreet;
    this.secondStreet = secondStreet;
  }

  public RestStreet getFirstStreet() {
    return firstStreet;
  }

  public RestStreet getSecondStreet() {
    return secondStreet;
  }
}
