package net.cabezudo.sofia.places;

import net.cabezudo.sofia.places.mappers.EntityToBusinessAdministrativeDivisionListMapper;
import net.cabezudo.sofia.places.mappers.EntityToBusinessAdministrativeDivisionMapper;
import net.cabezudo.sofia.places.persistence.AdministrativeDivisionEntity;
import net.cabezudo.sofia.places.persistence.AdministrativeDivisionEntityList;
import net.cabezudo.sofia.places.persistence.AdministrativeDivisionNameEntity;
import net.cabezudo.sofia.places.persistence.AdministrativeDivisionRepository;
import net.cabezudo.sofia.places.persistence.AdministrativeDivisionTypeEntity;
import net.cabezudo.sofia.places.persistence.AdministrativeDivisionTypeRepository;
import net.cabezudo.sofia.places.persistence.PlaceEntity;
import net.cabezudo.sofia.sites.Site;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

@Service
@Transactional
public class AdministrativeDivisionManager {
  private @Resource AdministrativeDivisionRepository administrativeDivisionRepository;
  private @Autowired EntityToBusinessAdministrativeDivisionListMapper entityToBusinessAdministrativeDivisionListMapper;
  private @Autowired EntityToBusinessAdministrativeDivisionMapper entityToBusinessAdministrativeDivisionEntityMapper;
  private @Autowired AdministrativeDivisionTypeRepository administrativeDivisionTypeRepository;

  public AdministrativeDivisionList findAll(Site site, int id) {
    final AdministrativeDivisionEntityList administrativeDivisionEntityList = administrativeDivisionRepository.findAll(site, id);
    return entityToBusinessAdministrativeDivisionListMapper.map(administrativeDivisionEntityList);
  }

  public AdministrativeDivision create(Site site, PlaceEntity place, AdministrativeDivisionNameEntity name, String typeName) {
    AdministrativeDivisionTypeEntity databaseTypeEntity = administrativeDivisionTypeRepository.findByName(site, typeName);
    AdministrativeDivisionTypeEntity typeEntity;
    if (databaseTypeEntity == null) {
      typeEntity = administrativeDivisionTypeRepository.create(site, typeName);
    } else {
      typeEntity = databaseTypeEntity;
    }
    AdministrativeDivisionEntity databaseEntity =
        administrativeDivisionRepository.findByPlaceNameAndType(site, place, name.getValue(), typeEntity);
    AdministrativeDivisionEntity entity;
    if (databaseEntity == null) {
      entity = administrativeDivisionRepository.create(site, place, typeEntity, name);
    } else {
      entity = databaseEntity;
    }

    return entityToBusinessAdministrativeDivisionEntityMapper.map(entity);
  }
}
