package net.cabezudo.sofia.places.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RestAdministrativeDivision {

  private int id;
  private RestAdministrativeDivisionType restAdministrativeDivisionType;
  private String name;

  public RestAdministrativeDivision(int id, RestAdministrativeDivisionType restAdministrativeDivisionType, String name) {
    this.id = id;
    this.restAdministrativeDivisionType = restAdministrativeDivisionType;
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setRestAdministrativeDivisionType(RestAdministrativeDivisionType restAdministrativeDivisionType) {
    this.restAdministrativeDivisionType = restAdministrativeDivisionType;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @JsonProperty("administrativeDivisionType")
  public RestAdministrativeDivisionType restAdministrativeDivisionType() {
    return restAdministrativeDivisionType;
  }
}
