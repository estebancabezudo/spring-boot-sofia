package net.cabezudo.sofia.places.rest;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RestAdministrativeDivision {

  private int id;
  private String type;
  private String name;

  public RestAdministrativeDivision(int id, String type, String name) {
    this.id = id;
    this.type = type;
    this.name = name;
  }
}
