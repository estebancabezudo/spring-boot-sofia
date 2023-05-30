package net.cabezudo.sofia.places.mappers;

import net.cabezudo.sofia.accounts.Account;
import net.cabezudo.sofia.places.Place;
import net.cabezudo.sofia.places.persistence.PlaceEntity;
import org.springframework.stereotype.Component;

@Component
public class BusinessToEntityPlaceMapper {
  public PlaceEntity map(Account account, Place p) {

    int id = p.id();
    String name = p.name();
    String street = p.street();
    String number = p.number();
    String interiorNumber = p.interiorNumber();
    String references = p.references();
    String postalCode = p.postalCode();
    int countryId = p.country().id();

    return new PlaceEntity(id, account.id(), name, street, number, interiorNumber, references, postalCode, countryId);
  }
}
