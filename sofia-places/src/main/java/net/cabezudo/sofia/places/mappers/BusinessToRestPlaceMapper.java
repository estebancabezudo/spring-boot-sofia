package net.cabezudo.sofia.places.mappers;

import net.cabezudo.sofia.places.Place;
import net.cabezudo.sofia.places.rest.AdministrativeDivisionsRestList;
import net.cabezudo.sofia.places.rest.RestPlace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BusinessToRestPlaceMapper {
  private @Autowired BusinessToRestAdminitrativeDivisionListMapper businessToRestAdminitrativeDivisionListMapper;

  public RestPlace map(Place p) {
    int id = p.getId();
    String name = p.getName();
    String street = p.getStreet();
    String number = p.getNumber();
    String interiorNumber = p.getInteriorNumber();
    String references = p.getReferences();
    String postalCode = p.getPostalCode();
    int countryId = p.getCountry().id();
    String countryCode = p.getCountry().code();

    AdministrativeDivisionsRestList administrativeDivisionList
        = businessToRestAdminitrativeDivisionListMapper.map(p.getAdministrativeDivisionList());

    return new RestPlace(id, name, street, number, interiorNumber, references, postalCode, countryId, countryCode, administrativeDivisionList);
  }
}
