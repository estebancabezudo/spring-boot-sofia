package net.cabezudo.sofia.places;

public class AdministrativeDivisionType {
  private final int id;
  private final String name;

  public AdministrativeDivisionType(int id, String name) {
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
