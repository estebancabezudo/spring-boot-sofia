package net.cabezudo.sofia.places;

import net.cabezudo.sofia.countries.Country;

import java.util.Objects;

public final class Place {
  private final int id;
  private final String name;
  private final String street;
  private final String number;
  private final String interiorNumber;
  private final String references;
  private final String postalCode;
  private final Country country;
  private final AdministrativeDivisionList administrativeDivisionList;

  public Place(
      int id, String name, String street, String number, String interiorNumber, String references, String postalCode, Country country, AdministrativeDivisionList administrativeDivisionList) {
    this.id = id;
    this.name = name;
    this.street = street;
    this.number = number;
    this.interiorNumber = interiorNumber;
    this.references = references;
    this.postalCode = postalCode;
    this.country = country;
    this.administrativeDivisionList = administrativeDivisionList;
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

  public Country getCountry() {
    return country;
  }

  public AdministrativeDivisionList getAdministrativeDivisionList() {
    return administrativeDivisionList;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (Place) obj;
    return this.id == that.id &&
        Objects.equals(this.name, that.name) &&
        Objects.equals(this.street, that.street) &&
        Objects.equals(this.number, that.number) &&
        Objects.equals(this.interiorNumber, that.interiorNumber) &&
        Objects.equals(this.references, that.references) &&
        Objects.equals(this.postalCode, that.postalCode) &&
        Objects.equals(this.country, that.country) &&
        Objects.equals(this.administrativeDivisionList, that.administrativeDivisionList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, street, number, interiorNumber, references, postalCode, country, administrativeDivisionList);
  }

  @Override
  public String toString() {
    return "Place[" +
        "id=" + id + ", " +
        "name=" + name + ", " +
        "street=" + street + ", " +
        "number=" + number + ", " +
        "interiorNumber=" + interiorNumber + ", " +
        "references=" + references + ", " +
        "postalCode=" + postalCode + ", " +
        "country=" + country + ", " +
        "administrativeDivisionList=" + administrativeDivisionList + ']';
  }
}

