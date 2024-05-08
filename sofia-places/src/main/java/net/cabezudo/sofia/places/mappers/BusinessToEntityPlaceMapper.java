package net.cabezudo.sofia.places.mappers;

import net.cabezudo.sofia.accounts.service.Account;
import net.cabezudo.sofia.places.Place;
import net.cabezudo.sofia.places.persistence.PlaceEntity;
import net.cabezudo.sofia.places.persistence.PostalCodeEntity;
import net.cabezudo.sofia.places.persistence.PostalCodeRepository;
import net.cabezudo.sofia.places.persistence.StreetEntity;
import net.cabezudo.sofia.places.persistence.StreetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class BusinessToEntityPlaceMapper {

  private @Autowired StreetRepository streetRepository;
  private @Autowired PostalCodeRepository postalCodeRepository;

  public PlaceEntity map(JdbcTemplate jdbcTemplate, Account account, Place place, Integer id) {
    if (place == null) {
      return null;
    }
    String name = place.getName();
    StreetEntity street;
    if (place.getStreet() == null) {
      street = null;
    } else {
      street = streetRepository.getByNameOrCreate(jdbcTemplate, place.getStreet());
    }
    String number = place.getNumber();
    String interiorNumber = place.getInteriorNumber();
    StreetEntity cornerStreet;
    if (place.getCornerStreet() == null) {
      cornerStreet = null;
    } else {
      cornerStreet = streetRepository.getByNameOrCreate(jdbcTemplate, place.getCornerStreet());
    }
    StreetEntity firstStreet;
    StreetEntity secondStreet;
    if (place.getBetweenStreets() == null) {
      firstStreet = null;
      secondStreet = null;
    } else {
      if (place.getBetweenStreets().getFirst() == null) {
        firstStreet = null;
      } else {
        firstStreet = streetRepository.getByNameOrCreate(jdbcTemplate, place.getBetweenStreets().getFirst());
      }
      if (place.getBetweenStreets().getSecond() == null) {
        secondStreet = null;
      } else {
        secondStreet = streetRepository.getByNameOrCreate(jdbcTemplate, place.getBetweenStreets().getSecond());
      }
    }

    String references = place.getReferences();
    PostalCodeEntity postalCode = postalCodeRepository.getByCodeOrCreate(jdbcTemplate, place.getPostalCode());
    int countryId = place.getCountry().getId();

    return new PlaceEntity(
        id, account.getId(), name, street == null ? null : street.getId(), number, interiorNumber,
        cornerStreet == null ? null : cornerStreet.getId(), firstStreet == null ? null : firstStreet.getId(),
        secondStreet == null ? null : secondStreet.getId(), references, postalCode == null ? null : postalCode.getId(), countryId);
  }
}
