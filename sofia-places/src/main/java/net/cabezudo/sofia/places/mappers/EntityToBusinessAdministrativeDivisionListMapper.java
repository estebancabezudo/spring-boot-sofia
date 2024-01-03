package net.cabezudo.sofia.places.mappers;

import net.cabezudo.sofia.places.AdministrativeDivision;
import net.cabezudo.sofia.places.AdministrativeDivisionList;
import net.cabezudo.sofia.places.AdministrativeDivisionType;
import net.cabezudo.sofia.places.persistence.AdministrativeDivisionEntity;
import net.cabezudo.sofia.places.persistence.AdministrativeDivisionEntityList;
import org.springframework.stereotype.Component;

@Component
public class EntityToBusinessAdministrativeDivisionListMapper {

  public AdministrativeDivisionList map(AdministrativeDivisionEntityList administrativeDivisionEntityList) {
    AdministrativeDivisionList administrativeDivisionList = new AdministrativeDivisionList();
    if (administrativeDivisionEntityList == null) {
      return null;
    }
    for (AdministrativeDivisionEntity administrativeDivisionEntity : administrativeDivisionEntityList) {
      int id = administrativeDivisionEntity.getId();
      int administrativeDivisionTypeEntityId = administrativeDivisionEntity.getAdministrativeDivisionTypeEntityId();
      String administrativeDivisionTypeEntityName = administrativeDivisionEntity.getAdministrativeDivisionTypeEntityName();
      AdministrativeDivisionType type = new AdministrativeDivisionType(administrativeDivisionTypeEntityId, administrativeDivisionTypeEntityName);
      String name = administrativeDivisionEntity.getName();
      AdministrativeDivision administrativeDivision = new AdministrativeDivision(id, type, name);
      administrativeDivisionList.add(administrativeDivision);
    }
    return administrativeDivisionList;
  }
}
