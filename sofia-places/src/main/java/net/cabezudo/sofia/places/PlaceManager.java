package net.cabezudo.sofia.places;

import net.cabezudo.sofia.accounts.Account;
import net.cabezudo.sofia.core.persistence.EntityList;
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
import net.cabezudo.sofia.sites.Site;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PlaceManager {
  private @Autowired BusinessToEntityPlaceMapper businessToEntityPlaceMapper;
  private @Autowired EntityToBusinessPlaceMapper entityToBusinessPlaceMapper;
  private @Resource AdministrativeDivisionRepository administrativeDivisionRepository;
  private @Autowired EntityToBusinessPlaceListMapper entityToBusinessPlaceListMapper;

  private @Resource PlacesRepository placesRepository;
  private @Resource AdministrativeDivisionNameRepository administrativeDivisionNameRepository;
  private @Resource AdministrativeDivisionTypeRepository administrativeDivisionTypeRepository;

  public PlaceList findAll(Site site, Account account, boolean includeAdministrativeDivisions) {
    final EntityList<PlaceEntity> entityList = placesRepository.findAll(site, account.id());

    if (includeAdministrativeDivisions) {
      List<Integer> placeIds = new ArrayList<>();
      // TODO use streams here
      for (PlaceEntity entity : entityList) {
        placeIds.add(entity.getId());
      }

      AdministrativeDivisionEntityList administrativeDivisionEntityList =
          administrativeDivisionRepository.findByPlaces(site, placeIds);
      return entityToBusinessPlaceListMapper.map(entityList, administrativeDivisionEntityList);
    }

    return entityToBusinessPlaceListMapper.map(entityList, null);
  }

  public Place get(Site site, Account account, Integer id) {
    final PlaceEntity placeEntity = placesRepository.get(site, account.id(), id);
    AdministrativeDivisionEntityList administrativeDivisionEntityList =
        administrativeDivisionRepository.findAll(site, id);

    return entityToBusinessPlaceMapper.map(placeEntity, administrativeDivisionEntityList);
  }

  public Place create(Site site, Account account, Place placeToSave) {
    PlaceEntity placeEntityToSave = businessToEntityPlaceMapper.map(account, placeToSave);

    final PlaceEntity placeEntity = placesRepository.create(site, placeEntityToSave);

    AdministrativeDivisionList administrativeDivisionListToSave = placeToSave.administrativeDivisionList();
    AdministrativeDivisionEntityList administrativeDivisionEntityList =
        createAdministrativeDivisions(site, administrativeDivisionListToSave, placeEntity);

    return entityToBusinessPlaceMapper.map(placeEntity, administrativeDivisionEntityList);
  }

  public Place update(Site site, Account account, Integer id, Place updatedPlace) {
    BusinessToEntityPlaceMapper businessToEntityPlaceMapper = new BusinessToEntityPlaceMapper();
    PlaceEntity placeEntityToSave = businessToEntityPlaceMapper.map(account, updatedPlace);
    placeEntityToSave.setId(id);

    final PlaceEntity placeEntity = placesRepository.update(site, placeEntityToSave);

    administrativeDivisionRepository.delete(site, placeEntity.getId());

    // Create all the administrative division again
    AdministrativeDivisionList administrativeDivisionListToSave = updatedPlace.administrativeDivisionList();
    AdministrativeDivisionEntityList administrativeDivisionEntityList =
        createAdministrativeDivisions(site, administrativeDivisionListToSave, placeEntity);

    return entityToBusinessPlaceMapper.map(placeEntity, administrativeDivisionEntityList);
  }

  private AdministrativeDivisionEntityList createAdministrativeDivisions(
      Site site, AdministrativeDivisionList administrativeDivisionListToSave, PlaceEntity placeEntity) {
    // Make sure that all the administrative division types exists in database


    AdministrativeDivisionEntityList administrativeDivisionEntityList = new AdministrativeDivisionEntityList();
    for (AdministrativeDivision entry : administrativeDivisionListToSave) {
      String name = entry.name();

      AdministrativeDivisionNameEntity databaseNameEntity = administrativeDivisionNameRepository.findByName(site, name);
      AdministrativeDivisionNameEntity nameEntity;
      if (databaseNameEntity == null) {
        nameEntity = administrativeDivisionNameRepository.create(site, name);
      } else {
        nameEntity = databaseNameEntity;
      }

      String typeName = entry.administrativeDivisionType().getName();

      AdministrativeDivisionTypeEntity databaseTypeEntity =
          administrativeDivisionTypeRepository.findByName(site, typeName);

      AdministrativeDivisionTypeEntity typeEntity;
      if (databaseTypeEntity == null) {
        typeEntity = administrativeDivisionTypeRepository.create(site, typeName);
      } else {
        typeEntity = databaseTypeEntity;
      }

      AdministrativeDivisionEntity databaseAdministrativeDivisionEntity =
          administrativeDivisionRepository.findByPlaceNameAndType(site, placeEntity, name, typeEntity);

      AdministrativeDivisionEntity administrativeDivisionEntity;
      if (databaseAdministrativeDivisionEntity == null) {
        administrativeDivisionEntity = administrativeDivisionRepository.create(site, placeEntity, typeEntity, nameEntity);
      } else {
        administrativeDivisionEntity = databaseAdministrativeDivisionEntity;
      }
      administrativeDivisionEntityList.add(administrativeDivisionEntity);
    }
    return administrativeDivisionEntityList;
  }

  public void delete(Site site, Account account, Integer id) {
    placesRepository.delete(site, account.id(), id);
  }
}
