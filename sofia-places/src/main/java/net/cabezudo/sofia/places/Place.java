package net.cabezudo.sofia.places;

import net.cabezudo.sofia.countries.Country;

import java.util.Objects;

public record Place(
    int id, String name, String street, String number, String interiorNumber, String references, String postalCode, Country country, AdministrativeDivisionList administrativeDivisionList) {
  public Place {
    Objects.requireNonNull(name);
    Objects.requireNonNull(street);
    Objects.requireNonNull(number);
    Objects.requireNonNull(postalCode);
    Objects.requireNonNull(country);
    Objects.requireNonNull(administrativeDivisionList);
  }
}

