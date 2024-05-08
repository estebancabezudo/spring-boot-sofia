package net.cabezudo.sofia.places.persistence;

import jakarta.persistence.Entity;
import lombok.Getter;


@Getter
@Entity
public class PlaceEntity {

  private final Integer id;
  private final int accountId;
  private final String name;
  private final Integer streetId;
  private final String number;
  private final String interiorNumber;
  private final Integer cornerStreetId;
  private final Integer firstStreetId;
  private final Integer secondStreetId;
  private final String references;
  private final Integer postalCodeId;
  private final int countryId;

  public PlaceEntity(Integer id, int accountId, String name, Integer streetId, String number, String interiorNumber, Integer cornerStreetId, Integer firstStreetId,
                     Integer secondStreetId, String references, Integer postalCodeId, int countryId) {
    this.id = id;
    this.accountId = accountId;
    this.name = name;
    this.streetId = streetId;
    this.number = number;
    this.interiorNumber = interiorNumber;
    this.cornerStreetId = cornerStreetId;
    this.firstStreetId = firstStreetId;
    this.secondStreetId = secondStreetId;
    this.references = references;
    this.postalCodeId = postalCodeId;
    this.countryId = countryId;
  }
}
