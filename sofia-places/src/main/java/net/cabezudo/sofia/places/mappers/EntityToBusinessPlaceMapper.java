package net.cabezudo.sofia.places.mappers;

import net.cabezudo.sofia.countries.Country;
import net.cabezudo.sofia.countries.CountryManager;
import net.cabezudo.sofia.places.AdministrativeDivisionList;
import net.cabezudo.sofia.places.BetweenStreets;
import net.cabezudo.sofia.places.Place;
import net.cabezudo.sofia.places.PostalCode;
import net.cabezudo.sofia.places.Street;
import net.cabezudo.sofia.places.persistence.AdministrativeDivisionEntityList;
import net.cabezudo.sofia.places.persistence.PlaceEntity;
import net.cabezudo.sofia.places.persistence.PostalCodeEntity;
import net.cabezudo.sofia.places.persistence.PostalCodeRepository;
import net.cabezudo.sofia.places.persistence.StreetEntity;
import net.cabezudo.sofia.places.persistence.StreetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class EntityToBusinessPlaceMapper {
  private @Autowired EntityToBusinessAdministrativeDivisionListMapper entityToBusinessAdministrativeDivisionListMapper;
  private @Autowired EntityToBusinessStreetMapper entityToBusinessStreetMapper;
  private @Autowired CountryManager countryManager;
  private @Autowired StreetRepository streetRepository;
  private @Autowired PostalCodeRepository postalCodeRepository;
  private @Autowired EntityToBusinessPostalCodeMapper entityToBusinessPostalCodeMapper;

  public Place map(JdbcTemplate jdbcTemplate, PlaceEntity placeEntity, AdministrativeDivisionEntityList administrativeDivisionEntityList) {
    if (placeEntity == null) {
      return null;
    }
    int id = placeEntity.getId();
    String name = placeEntity.getName();

    StreetEntity streetEntity = streetRepository.get(jdbcTemplate, placeEntity.getStreetId());
    Street street = entityToBusinessStreetMapper.map(streetEntity);

    StreetEntity cornerStreetEntity = streetRepository.get(jdbcTemplate, placeEntity.getCornerStreetId());
    Street cornerStreet = entityToBusinessStreetMapper.map(cornerStreetEntity);

    StreetEntity firstStreetEntity = streetRepository.get(jdbcTemplate, placeEntity.getFirstStreetId());
    Street firstStreet = entityToBusinessStreetMapper.map(firstStreetEntity);

    StreetEntity secondStreetEntity = streetRepository.get(jdbcTemplate, placeEntity.getSecondStreetId());
    Street secondStreet = entityToBusinessStreetMapper.map(secondStreetEntity);

    BetweenStreets betweenStreets = new BetweenStreets(firstStreet, secondStreet);

    String number = placeEntity.getNumber();
    String interiorNumber = placeEntity.getInteriorNumber();
    String references = placeEntity.getReferences();

    PostalCodeEntity postalCodeEntity = postalCodeRepository.get(jdbcTemplate, placeEntity.getPostalCodeId());
    PostalCode postalCode = entityToBusinessPostalCodeMapper.map(postalCodeEntity);

    Country country = countryManager.get(placeEntity.getCountryId());

    AdministrativeDivisionList administrativeDivisionList =
        entityToBusinessAdministrativeDivisionListMapper.map(administrativeDivisionEntityList);
    return new Place(id, name, street, number, interiorNumber, cornerStreet, betweenStreets, references, postalCode, country, administrativeDivisionList);
  }
}
