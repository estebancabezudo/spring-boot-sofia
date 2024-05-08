package net.cabezudo.sofia.places.mappers;

import net.cabezudo.sofia.countries.Country;
import net.cabezudo.sofia.countries.CountryManager;
import net.cabezudo.sofia.places.AdministrativeDivisionList;
import net.cabezudo.sofia.places.BetweenStreets;
import net.cabezudo.sofia.places.Place;
import net.cabezudo.sofia.places.PostalCode;
import net.cabezudo.sofia.places.Street;
import net.cabezudo.sofia.places.rest.RestBetweenStreets;
import net.cabezudo.sofia.places.rest.RestPlace;
import net.cabezudo.sofia.places.rest.RestStreet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RestToBusinessPlaceMapper {
  private @Autowired CountryManager countryManager;
  private @Autowired RestToBusinessAdministrativeDivisionListMapper restToBusinessAdministrativeDivisionListMapper;

  public Place map(RestPlace restPlace) {
    if (restPlace == null) {
      return null;
    }
    Country country = countryManager.get(restPlace.getCountryCode());
    AdministrativeDivisionList administrativeDivisionList = restToBusinessAdministrativeDivisionListMapper.map(restPlace.getAdministrativeDivisions());
    RestStreet restStreet = restPlace.getStreet();
    Street street;
    if (restStreet == null) {
      street = null;
    } else {
      street = new Street(restStreet.getId(), restStreet.getName(), restStreet.isVerified());
    }
    RestStreet restCornerStreet = restPlace.getCornerStreet();
    Street cornerStreet;
    if (restCornerStreet == null) {
      cornerStreet = null;
    } else {
      cornerStreet = new Street(restCornerStreet.getId(), restCornerStreet.getName(), restCornerStreet.isVerified());
    }
    RestBetweenStreets restBetweenStreet = restPlace.getBetweenStreets();
    BetweenStreets betweenStreets;
    Street firstStreet;
    Street secondStreet;
    if (restBetweenStreet == null) {
      betweenStreets = new BetweenStreets(null, null);
    } else {
      RestStreet restFirstStreet = restBetweenStreet.getFirstStreet();
      if (restFirstStreet == null) {
        firstStreet = null;
      } else {
        firstStreet = new Street(restFirstStreet.getId(), restFirstStreet.getName(), restFirstStreet.isVerified());
      }
      RestStreet secondRestStreet = restBetweenStreet.getSecondStreet();
      if (secondRestStreet == null) {
        secondStreet = null;
      } else {
        secondStreet = new Street(secondRestStreet.getId(), secondRestStreet.getName(), secondRestStreet.isVerified());
      }
      betweenStreets = new BetweenStreets(firstStreet, secondStreet);
    }
    PostalCode postalCode = restPlace.getPostalCode() == null ? null : new PostalCode(restPlace.getPostalCode(), false);

    return new Place(
        restPlace.getId(), restPlace.getName(), street, restPlace.getNumber(), restPlace.getInteriorNumber(), cornerStreet, betweenStreets, restPlace.getReferences(),
        postalCode, country, administrativeDivisionList);
  }
}
