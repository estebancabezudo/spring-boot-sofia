package net.cabezudo.sofia.places.rest;

public class RestAdministrativeDivisionType {
  private final int id;
  private final String name;

  public RestAdministrativeDivisionType(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}
