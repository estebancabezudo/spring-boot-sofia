package net.cabezudo.sofia.places.mappers;

import net.cabezudo.sofia.places.AdministrativeDivisionType;
import net.cabezudo.sofia.places.rest.RestAdministrativeDivisionType;

public class RestToBusinessAdministrativeDivisionTypeMapper {
  public AdministrativeDivisionType map(RestAdministrativeDivisionType restAdministrativeDivisionType) {
    int id = restAdministrativeDivisionType.getId();
    String name = restAdministrativeDivisionType.getName();
    return new AdministrativeDivisionType(id, name);
  }
}
