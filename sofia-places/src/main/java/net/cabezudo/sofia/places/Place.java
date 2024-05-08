package net.cabezudo.sofia.places;

import lombok.Getter;
import net.cabezudo.sofia.countries.Country;

import java.util.Objects;

public final class Place {
  @Getter
  private final Integer id;
  @Getter
  private final String name;
  @Getter
  private final Street street;
  @Getter
  private final String number;
  @Getter
  private final String interiorNumber;
  @Getter
  private final Street cornerStreet;
  @Getter
  private final BetweenStreets betweenStreets;
  @Getter
  private final String references;
  @Getter
  private final PostalCode postalCode;
  @Getter
  private final Country country;
  @Getter
  private final AdministrativeDivisionList administrativeDivisionList;

  public Place(
      Integer id, String name, Street street, String number, String interiorNumber, Street cornerStreet, BetweenStreets betweenStreets,
      String references, PostalCode postalCode, Country country, AdministrativeDivisionList administrativeDivisionList) {
    this.id = id;
    this.name = name;
    this.street = street;
    this.number = number;
    this.interiorNumber = interiorNumber;
    this.betweenStreets = betweenStreets;
    this.cornerStreet = cornerStreet;
    this.references = references;
    this.postalCode = postalCode;
    this.country = country;
    this.administrativeDivisionList = administrativeDivisionList;
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

