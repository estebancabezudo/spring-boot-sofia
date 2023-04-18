package net.cabezudo.sofia.places.mappers;

import net.cabezudo.sofia.places.AdministrativeDivisionType;
import net.cabezudo.sofia.places.rest.RestAdministrativeDivisionType;
import org.springframework.stereotype.Component;

@Component
public class BusinessToRestAdminitrativeDivisionTypeMapper {
  public RestAdministrativeDivisionType map(AdministrativeDivisionType administrativeDivisionType) {
    int id = administrativeDivisionType.getId();
    String name = administrativeDivisionType.getName();
    return new RestAdministrativeDivisionType(id, name);
  }
}
