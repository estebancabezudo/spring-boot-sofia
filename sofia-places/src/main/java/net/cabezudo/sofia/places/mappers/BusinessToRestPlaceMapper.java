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
    int id = p.id();
    String name = p.name();
    String street = p.street();
    String number = p.number();
    String interiorNumber = p.interiorNumber();
    String references = p.references();
    String postalCode = p.postalCode();
    int countryId = p.country().getId();
    String countryCode = p.country().getCode();

    AdministrativeDivisionsRestList administrativeDivisionList
        = businessToRestAdminitrativeDivisionListMapper.map(p.administrativeDivisionList());

    return new RestPlace(id, name, street, number, interiorNumber, references, postalCode, countryId, countryCode, administrativeDivisionList);
  }
}
