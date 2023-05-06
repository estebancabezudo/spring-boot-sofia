package net.cabezudo.sofia.places.mappers;

import net.cabezudo.sofia.countries.Country;
import net.cabezudo.sofia.countries.CountryManager;
import net.cabezudo.sofia.places.AdministrativeDivisionList;
import net.cabezudo.sofia.places.Place;
import net.cabezudo.sofia.places.persistence.AdministrativeDivisionEntityList;
import net.cabezudo.sofia.places.persistence.PlaceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EntityToBusinessPlaceMapper {
  private @Autowired EntityToBusinessAdministrativeDivisionListMapper entityToBusinessAdministrativeDivisionListMapper;
  private @Autowired CountryManager countryManager;

  public Place map(PlaceEntity p, AdministrativeDivisionEntityList administrativeDivisionEntityList) {
    int id = p.getId();
    String name = p.getName();
    String street = p.getStreet();
    String number = p.getNumber();
    String interiorNumber = p.getInteriorNumber();
    String references = p.getReferences();
    String postalCode = p.getPostalCode();
    Country country = countryManager.get(p.getCountryId());

    AdministrativeDivisionList administrativeDivisionList =
        entityToBusinessAdministrativeDivisionListMapper.map(administrativeDivisionEntityList);
    return new Place(id, name, street, number, interiorNumber, references, postalCode, country, administrativeDivisionList);
  }
}
