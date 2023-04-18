package net.cabezudo.sofia.places.persistence;

public class AdministrativeDivisionTypeEntity {
  private final Integer id;
  private final String name;

  public AdministrativeDivisionTypeEntity(Integer id, String name) {
    this.id = id;
    this.name = name;
  }

  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}
