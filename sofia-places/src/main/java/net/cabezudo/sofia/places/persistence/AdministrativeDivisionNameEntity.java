package net.cabezudo.sofia.places.persistence;

public class AdministrativeDivisionNameEntity {

  private final int id;
  private final String value;

  public AdministrativeDivisionNameEntity(int id, String value) {
    this.id = id;
    this.value = value;
  }

  public int getId() {
    return id;
  }

  public String getValue() {
    return value;
  }
}
