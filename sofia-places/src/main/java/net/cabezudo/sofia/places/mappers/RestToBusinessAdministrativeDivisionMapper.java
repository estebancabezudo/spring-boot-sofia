package net.cabezudo.sofia.places.mappers;

import net.cabezudo.sofia.places.AdministrativeDivision;
import net.cabezudo.sofia.places.AdministrativeDivisionType;
import net.cabezudo.sofia.places.rest.RestAdministrativeDivision;


public class RestToBusinessAdministrativeDivisionMapper {
  public AdministrativeDivision map(RestAdministrativeDivision restAdministrativeDivision) {
    int id = restAdministrativeDivision.getId();
    RestToBusinessAdministrativeDivisionTypeMapper mapper = new RestToBusinessAdministrativeDivisionTypeMapper();
    AdministrativeDivisionType administrativeDivisionType = mapper.map(restAdministrativeDivision.restAdministrativeDivisionType());
    String name = restAdministrativeDivision.getName();
    return new AdministrativeDivision(id, administrativeDivisionType, name);
  }
}
