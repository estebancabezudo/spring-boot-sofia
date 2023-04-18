package net.cabezudo.sofia.places.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RestPlace {

  private final int id;
  private final String name;
  private final String street;
  private final String number;
  private final String interiorNumber;
  private final String references;
  private final String postalCode;
  private final String countryCode;
  private AdministrativeDivisionsRestList administrativeDivisionsRestList;

  public RestPlace(int id, String name, String street, String number, String interiorNumber, String references, String postalCode, int countryId, String countryCode, AdministrativeDivisionsRestList administrativeDivisionsRestList) {
    this.id = id;
    this.name = name;
    this.street = street;
    this.number = number;
    this.interiorNumber = interiorNumber;
    this.references = references;
    this.postalCode = postalCode;
    this.countryCode = countryCode;
    this.administrativeDivisionsRestList = administrativeDivisionsRestList;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getStreet() {
    return street;
  }

  public String getNumber() {
    return number;
  }

  public String getInteriorNumber() {
    return interiorNumber;
  }

  public String getReferences() {
    return references;
  }

  public String getPostalCode() {
    return postalCode;
  }
  
  public String getCountryCode() {
    return countryCode;
  }

  @JsonProperty("administrativeDivisions")
  public AdministrativeDivisionsRestList getAdministrativeDivisionRestList() {
    return administrativeDivisionsRestList;
  }

  @JsonProperty("administrativeDivisions")
  public void setAdministrativeDivisionsRestList(AdministrativeDivisionsRestList administrativeDivisionsRestList) {
    this.administrativeDivisionsRestList = administrativeDivisionsRestList;
  }
}
