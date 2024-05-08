package net.cabezudo.sofia.places.mappers;

import net.cabezudo.sofia.core.persistence.EntityList;
import net.cabezudo.sofia.places.PlaceList;
import net.cabezudo.sofia.places.persistence.AdministrativeDivisionEntity;
import net.cabezudo.sofia.places.persistence.AdministrativeDivisionEntityList;
import net.cabezudo.sofia.places.persistence.PlaceEntity;
import net.cabezudo.sofia.places.persistence.StreetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.TreeMap;

@Component
public class EntityToBusinessPlaceListMapper {
  private @Autowired EntityToBusinessPlaceMapper entityToBusinessPlaceMapper;
  private @Autowired StreetRepository streetRepository;

  public PlaceList map(EntityList<PlaceEntity> entityList, AdministrativeDivisionEntityList administrativeDivisionEntityList) {
    int total = entityList.getTotal();
    int start = entityList.getStart();
    int size = entityList.size();
    PlaceList list = new PlaceList(total, start, size);

    Map<Integer, AdministrativeDivisionEntityList> map;

    if (administrativeDivisionEntityList != null) {
      map = getMap(administrativeDivisionEntityList);
    } else {
      map = null;
    }

    JdbcTemplate jdbcTemplate = streetRepository.getJDBCTemplate();

    for (PlaceEntity entityPlace : entityList) {
      AdministrativeDivisionEntityList administrativeDivisionEntityListFromMap;
      if (administrativeDivisionEntityList != null) {
        administrativeDivisionEntityListFromMap = map.get(entityPlace.getId());
      } else {
        administrativeDivisionEntityListFromMap = null;
      }
      list.add(entityToBusinessPlaceMapper.map(jdbcTemplate, entityPlace, administrativeDivisionEntityListFromMap));
    }
    return list;
  }

  private Map<Integer, AdministrativeDivisionEntityList> getMap(AdministrativeDivisionEntityList administrativeDivisionEntityList) {
    Map<Integer, AdministrativeDivisionEntityList> map = new TreeMap<>();
    if (administrativeDivisionEntityList == null) {
      return null;
    }

    for (AdministrativeDivisionEntity entity : administrativeDivisionEntityList) {
      AdministrativeDivisionEntityList listInMap = map.get(entity.getPlaceId());
      if (listInMap == null) {
        AdministrativeDivisionEntityList newList = new AdministrativeDivisionEntityList();
        newList.add(entity);
        map.put(entity.getPlaceId(), newList);
      } else {
        listInMap.add(entity);
      }
    }
    return map;
  }
}
