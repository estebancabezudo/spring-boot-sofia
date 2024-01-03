package net.cabezudo.sofia.places.mappers;

import net.cabezudo.sofia.countries.Country;
import net.cabezudo.sofia.countries.CountryManager;
import net.cabezudo.sofia.places.AdministrativeDivisionList;
import net.cabezudo.sofia.places.Place;
import net.cabezudo.sofia.places.rest.RestPlace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RestToBusinessPlaceMapper {
  private @Autowired CountryManager countryManager;

  public Place map(RestPlace p) {
    Country country = countryManager.get(p.getId());
    RestToBusinessAdministrativeDivisionListMapper mapper = new RestToBusinessAdministrativeDivisionListMapper();
    AdministrativeDivisionList administrativeDivisionList = mapper.map(p.getAdministrativeDivisionRestList());
    return new Place(p.getId(), p.getName(), p.getStreet(), p.getNumber(), p.getInteriorNumber(), p.getReferences(), p.getPostalCode(), country, administrativeDivisionList);
  }
}
