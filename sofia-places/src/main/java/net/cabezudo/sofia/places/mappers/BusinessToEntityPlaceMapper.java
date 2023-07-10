package net.cabezudo.sofia.places.mappers;

import net.cabezudo.sofia.accounts.service.Account;
import net.cabezudo.sofia.places.Place;
import net.cabezudo.sofia.places.persistence.PlaceEntity;
import org.springframework.stereotype.Component;

@Component
public class BusinessToEntityPlaceMapper {
  public PlaceEntity map(Account account, Place p) {

    int id = p.getId();
    String name = p.getName();
    String street = p.getStreet();
    String number = p.getNumber();
    String interiorNumber = p.getInteriorNumber();
    String references = p.getReferences();
    String postalCode = p.getPostalCode();
    int countryId = p.getCountry().id();

    return new PlaceEntity(id, account.getId(), name, street, number, interiorNumber, references, postalCode, countryId);
  }
}
