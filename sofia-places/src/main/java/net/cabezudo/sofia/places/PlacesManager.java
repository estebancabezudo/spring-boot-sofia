package net.cabezudo.sofia.places;

import jakarta.transaction.Transactional;
import net.cabezudo.sofia.accounts.service.Account;
import net.cabezudo.sofia.core.persistence.EntityList;
import net.cabezudo.sofia.persistence.DatabaseManager;
import net.cabezudo.sofia.places.mappers.BusinessToEntityPlaceMapper;
import net.cabezudo.sofia.places.mappers.EntityToBusinessPlaceListMapper;
import net.cabezudo.sofia.places.mappers.EntityToBusinessPlaceMapper;
import net.cabezudo.sofia.places.persistence.AdministrativeDivisionEntity;
import net.cabezudo.sofia.places.persistence.AdministrativeDivisionEntityList;
import net.cabezudo.sofia.places.persistence.AdministrativeDivisionNameEntity;
import net.cabezudo.sofia.places.persistence.AdministrativeDivisionNameRepository;
import net.cabezudo.sofia.places.persistence.AdministrativeDivisionRepository;
import net.cabezudo.sofia.places.persistence.AdministrativeDivisionTypeEntity;
import net.cabezudo.sofia.places.persistence.AdministrativeDivisionTypeRepository;
import net.cabezudo.sofia.places.persistence.PlaceEntity;
import net.cabezudo.sofia.places.persistence.PlacesRepository;
import net.cabezudo.sofia.places.persistence.StreetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PlacesManager {
  private @Autowired BusinessToEntityPlaceMapper businessToEntityPlaceMapper;
  private @Autowired EntityToBusinessPlaceMapper entityToBusinessPlaceMapper;
  private @Autowired AdministrativeDivisionRepository administrativeDivisionRepository;
  private @Autowired EntityToBusinessPlaceListMapper entityToBusinessPlaceListMapper;
  private @Autowired PlacesRepository placesRepository;
  private @Autowired AdministrativeDivisionNameRepository administrativeDivisionNameRepository;
  private @Autowired AdministrativeDivisionTypeRepository administrativeDivisionTypeRepository;
  private @Autowired StreetRepository streetRepository;
  private @Autowired DatabaseManager databaseManager;

  public PlaceList findAll(Account account, boolean includeAdministrativeDivisions) {
    final EntityList<PlaceEntity> entityList = placesRepository.findAll(account.getId());

    if (includeAdministrativeDivisions) {
      List<Integer> placeIds = new ArrayList<>();
      // TODO use streams here
      for (PlaceEntity entity : entityList) {
        placeIds.add(entity.getId());
      }

      AdministrativeDivisionEntityList administrativeDivisionEntityList =
          administrativeDivisionRepository.findByPlaces(placeIds);
      return entityToBusinessPlaceListMapper.map(entityList, administrativeDivisionEntityList);
    }

    return entityToBusinessPlaceListMapper.map(entityList, null);
  }

  public Place get(Integer id) {
    JdbcTemplate jdbcTemplate = databaseManager.getJDBCTemplate();
    final PlaceEntity placeEntity = placesRepository.get(jdbcTemplate, id);
    AdministrativeDivisionEntityList administrativeDivisionEntityList =
        administrativeDivisionRepository.findAll(id);
    return entityToBusinessPlaceMapper.map(jdbcTemplate, placeEntity, administrativeDivisionEntityList);
  }

  public Place create(Account account, Place placeToSave) {
    return this.create(databaseManager.getJDBCTemplate(), account, placeToSave);
  }

  public Place create(JdbcTemplate jdbcTemplate, Account account, Place placeToSave) {

    PlaceEntity entity = businessToEntityPlaceMapper.map(jdbcTemplate, account, placeToSave, null);

    int accountId = account.getId();
    String name = entity.getName();
    Integer streetId = entity.getStreetId();
    String number = entity.getNumber();
    String interiorNumber = entity.getInteriorNumber();
    Integer cornerStreetId = entity.getCornerStreetId();
    Integer firstStreetId = entity.getFirstStreetId();
    Integer secondStreetId = entity.getSecondStreetId();
    String references = entity.getReferences();
    Integer postalCodeId = entity.getPostalCodeId();
    int countryId = entity.getCountryId();

    PlaceEntity placeEntity = placesRepository.create(jdbcTemplate, accountId, name, streetId, number, interiorNumber, cornerStreetId, firstStreetId, secondStreetId, references, postalCodeId, countryId);
    AdministrativeDivisionList administrativeDivisionListToSave = placeToSave.getAdministrativeDivisionList();
    AdministrativeDivisionEntityList administrativeDivisionEntityList = createAdministrativeDivisions(administrativeDivisionListToSave, placeEntity);

    return entityToBusinessPlaceMapper.map(jdbcTemplate, placeEntity, administrativeDivisionEntityList);
  }

  public Place update(Account account, Integer id, Place updatedPlace) {
    JdbcTemplate jdbcTemplate = databaseManager.getJDBCTemplate();

    PlaceEntity placeEntityToSave = businessToEntityPlaceMapper.map(jdbcTemplate, account, updatedPlace, id);

    final PlaceEntity placeEntity = placesRepository.update(placeEntityToSave);

    administrativeDivisionRepository.delete(placeEntity.getId());
    // Create all the administrative division again
    AdministrativeDivisionList administrativeDivisionListToSave = updatedPlace.getAdministrativeDivisionList();
    AdministrativeDivisionEntityList administrativeDivisionEntityList = createAdministrativeDivisions(administrativeDivisionListToSave, placeEntity);

    return entityToBusinessPlaceMapper.map(jdbcTemplate, placeEntity, administrativeDivisionEntityList);
  }

  private AdministrativeDivisionEntityList createAdministrativeDivisions(AdministrativeDivisionList administrativeDivisionListToSave, PlaceEntity placeEntity) {
    AdministrativeDivisionEntityList administrativeDivisionEntityList = new AdministrativeDivisionEntityList();
    for (AdministrativeDivision administrativeDivision : administrativeDivisionListToSave) {
      AdministrativeDivisionNameEntity administrativeDivisionNameEntity = administrativeDivisionNameRepository.findByNameOrCreate(administrativeDivision.getName());
      AdministrativeDivisionTypeEntity administrativeDivisionTypeEntity = administrativeDivisionTypeRepository.findByNameOrCreate(administrativeDivision.getType().getName());
      AdministrativeDivisionEntity administrativeDivisionEntity = administrativeDivisionRepository.findByPlaceNameAndType(placeEntity, administrativeDivisionTypeEntity, administrativeDivisionNameEntity);
      administrativeDivisionEntityList.add(administrativeDivisionEntity);
    }
    return administrativeDivisionEntityList;
  }

  public void delete(Account account, Integer id) {
    placesRepository.delete(account.getId(), id);
  }

  public Place findOrCreate(JdbcTemplate jdbcTemplate, Account account, Place placeToFindOrCreate) {
    Place place = this.find(placeToFindOrCreate);
    if (place == null) {
      return this.create(jdbcTemplate, account, placeToFindOrCreate);
    }
    return place;
  }

  private Place find(Place placeToFind) {
    // TODO search the place using the diferent data. Remember that a place can be the same but with other data.
    return null;
  }
}
