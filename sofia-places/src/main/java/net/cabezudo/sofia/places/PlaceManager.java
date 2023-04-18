package net.cabezudo.sofia.places;

import net.cabezudo.sofia.accounts.Account;
import net.cabezudo.sofia.core.persistence.EntityList;
import net.cabezudo.sofia.countries.CountryManager;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PlaceManager {
  @Autowired
  private BusinessToEntityPlaceMapper businessToEntityPlaceMapper;
  @Autowired
  private EntityToBusinessPlaceMapper entityToBusinessPlaceMapper;
  @Resource
  private AdministrativeDivisionRepository administrativeDivisionRepository;
  @Autowired
  private EntityToBusinessPlaceListMapper entityToBusinessPlaceListMapper;
  @Resource
  private PlacesRepository placesRepository;
  @Resource
  private CountryManager countryManager;
  @Resource
  private AdministrativeDivisionNameRepository administrativeDivisionNameRepository;
  @Resource
  private AdministrativeDivisionTypeRepository administrativeDivisionTypeRepository;

  public PlaceList findAll(Account account, boolean includeAdministrativeDivisions) {
    final EntityList<PlaceEntity> entityList = placesRepository.findAll(account.id());

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

  public Place get(Account account, Integer id) {
    final PlaceEntity placeEntity = placesRepository.get(account.id(), id);
    AdministrativeDivisionEntityList administrativeDivisionEntityList =
        administrativeDivisionRepository.findAll(account.id(), id);

    return entityToBusinessPlaceMapper.map(placeEntity, administrativeDivisionEntityList);
  }

  public Place create(Account account, Place placeToSave) {
    PlaceEntity placeEntityToSave = businessToEntityPlaceMapper.map(account, placeToSave);

    final PlaceEntity placeEntity = placesRepository.create(placeEntityToSave);

    AdministrativeDivisionList administrativeDivisionListToSave = placeToSave.administrativeDivisionList();
    AdministrativeDivisionEntityList administrativeDivisionEntityList =
        createAdministrativeDivisions(administrativeDivisionListToSave, placeEntity);

    return entityToBusinessPlaceMapper.map(placeEntity, administrativeDivisionEntityList);
  }

  public Place update(Account account, Integer id, Place updatedPlace) {
    BusinessToEntityPlaceMapper businessToEntityPlaceMapper = new BusinessToEntityPlaceMapper();
    PlaceEntity placeEntityToSave = businessToEntityPlaceMapper.map(account, updatedPlace);
    placeEntityToSave.setId(id);

    final PlaceEntity placeEntity = placesRepository.update(placeEntityToSave);

    administrativeDivisionRepository.delete(placeEntity.getId());

    // Create all the administrative division again
    AdministrativeDivisionList administrativeDivisionListToSave = updatedPlace.administrativeDivisionList();
    AdministrativeDivisionEntityList administrativeDivisionEntityList =
        createAdministrativeDivisions(administrativeDivisionListToSave, placeEntity);

    return entityToBusinessPlaceMapper.map(placeEntity, administrativeDivisionEntityList);
  }

  private AdministrativeDivisionEntityList createAdministrativeDivisions(
      AdministrativeDivisionList administrativeDivisionListToSave, PlaceEntity placeEntity) {
    // Make sure that all the administrative division types exists in database


    AdministrativeDivisionEntityList administrativeDivisionEntityList = new AdministrativeDivisionEntityList();
    for (AdministrativeDivision entry : administrativeDivisionListToSave) {
      String name = entry.name();

      AdministrativeDivisionNameEntity databaseNameEntity = administrativeDivisionNameRepository.findByName(name);
      AdministrativeDivisionNameEntity nameEntity;
      if (databaseNameEntity == null) {
        nameEntity = administrativeDivisionNameRepository.create(name);
      } else {
        nameEntity = databaseNameEntity;
      }

      String typeName = entry.administrativeDivisionType().getName();

      AdministrativeDivisionTypeEntity databaseTypeEntity =
          administrativeDivisionTypeRepository.findByName(typeName);

      AdministrativeDivisionTypeEntity typeEntity;
      if (databaseTypeEntity == null) {
        typeEntity = administrativeDivisionTypeRepository.create(typeName);
      } else {
        typeEntity = databaseTypeEntity;
      }

      AdministrativeDivisionEntity databaseAdministrativeDivisionEntity =
          administrativeDivisionRepository.findByPlaceNameAndType(placeEntity, name, typeEntity);

      AdministrativeDivisionEntity administrativeDivisionEntity;
      if (databaseAdministrativeDivisionEntity == null) {
        administrativeDivisionEntity = administrativeDivisionRepository.create(placeEntity, typeEntity, nameEntity);
      } else {
        administrativeDivisionEntity = databaseAdministrativeDivisionEntity;
      }
      administrativeDivisionEntityList.add(administrativeDivisionEntity);
    }
    return administrativeDivisionEntityList;
  }

  public void delete(Account account, Integer id) {
    placesRepository.delete(account.id(), id);
  }
}
