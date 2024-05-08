package net.cabezudo.sofia.places;

import lombok.Getter;

@Getter
public class AdministrativeDivisionType {
  private final Integer id;
  private final String name;

  public AdministrativeDivisionType(Integer id, String name) {
    this.id = id;
    this.name = name;
  }
}
