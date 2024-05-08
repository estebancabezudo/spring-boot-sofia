package net.cabezudo.sofia.places.mappers;

import net.cabezudo.sofia.places.BetweenStreets;
import net.cabezudo.sofia.places.Place;
import net.cabezudo.sofia.places.Street;
import net.cabezudo.sofia.places.rest.AdministrativeDivisionsRestList;
import net.cabezudo.sofia.places.rest.RestBetweenStreets;
import net.cabezudo.sofia.places.rest.RestCornerStreet;
import net.cabezudo.sofia.places.rest.RestPlace;
import net.cabezudo.sofia.places.rest.RestStreet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BusinessToRestPlaceMapper {
  private @Autowired BusinessToRestAdministrativeDivisionListMapper businessToRestAdministrativeDivisionListMapper;

  public RestPlace map(Place place) {
    if (place == null) {
      return null;
    }
    Integer id = place.getId();
    String name = place.getName();
    Street street = place.getStreet();
    RestStreet restStreet = new RestStreet(street.getId(), street.getName(), street.isVerified());
    String number = place.getNumber();
    String interiorNumber = place.getInteriorNumber();
    Street cornerStreet = place.getCornerStreet();
    RestCornerStreet restCornerStreet;
    if (cornerStreet == null) {
      restCornerStreet = null;
    } else {
      restCornerStreet = new RestCornerStreet(cornerStreet.getId(), cornerStreet.getName(), cornerStreet.isVerified());
    }
    BetweenStreets betweenStreets = place.getBetweenStreets();
    RestBetweenStreets restBetweenStreets;
    if (betweenStreets == null) {
      restBetweenStreets = null;
    } else {
      Street firstStreet = betweenStreets.getFirst();
      Street secondStreet = betweenStreets.getSecond();
      RestStreet restFirstStreet;
      if (firstStreet == null) {
        restFirstStreet = null;
      } else {
        restFirstStreet = new RestStreet(firstStreet.getId(), firstStreet.getName(), firstStreet.isVerified());
      }
      RestStreet restSecondStreet;
      if (secondStreet == null) {
        restSecondStreet = null;
      } else {
        restSecondStreet = new RestStreet(secondStreet.getId(), secondStreet.getName(), secondStreet.isVerified());
      }
      restBetweenStreets = new RestBetweenStreets(restFirstStreet, restSecondStreet);
    }
    String references = place.getReferences();
    String postalCode = place.getPostalCode() == null ? null : place.getPostalCode().getCode();
    String countryCode = place.getCountry() == null ? null : place.getCountry().getCode();

    AdministrativeDivisionsRestList administrativeDivisionList = businessToRestAdministrativeDivisionListMapper.map(place.getAdministrativeDivisionList());

    return new RestPlace(id, name, restStreet, number, interiorNumber, restCornerStreet, restBetweenStreets, references, postalCode, countryCode, administrativeDivisionList);
  }
}
