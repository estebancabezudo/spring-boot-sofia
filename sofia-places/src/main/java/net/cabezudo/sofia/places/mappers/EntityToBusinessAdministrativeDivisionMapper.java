package net.cabezudo.sofia.places.mappers;

import net.cabezudo.sofia.places.AdministrativeDivision;
import net.cabezudo.sofia.places.AdministrativeDivisionType;
import net.cabezudo.sofia.places.persistence.AdministrativeDivisionEntity;
import org.springframework.stereotype.Component;

@Component
public class EntityToBusinessAdministrativeDivisionMapper {

  public AdministrativeDivision map(AdministrativeDivisionEntity administrativeDivisionEntity) {
    int typeId = administrativeDivisionEntity.getAdministrativeDivisionTypeEntityId();
    String typeName = administrativeDivisionEntity.getAdministrativeDivisiónTypeEntityName();
    AdministrativeDivisionType type = new AdministrativeDivisionType(typeId, typeName);
    String name = administrativeDivisionEntity.getName();
    return new AdministrativeDivision(null, type, name);
  }
}
