package net.cabezudo.sofia.places.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class RestPlace {
  private final Integer id;
  private final String name;
  private final RestStreet street;
  private final String number;
  private final String interiorNumber;
  private final RestCornerStreet cornerStreet;
  private final RestBetweenStreets betweenStreets;
  private final String references;
  private final String postalCode;
  private final String countryCode;
  private AdministrativeDivisionsRestList administrativeDivisions;
}