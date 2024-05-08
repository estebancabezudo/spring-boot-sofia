package net.cabezudo.sofia.places.mappers;

import net.cabezudo.sofia.places.AdministrativeDivision;
import net.cabezudo.sofia.places.AdministrativeDivisionType;
import net.cabezudo.sofia.places.rest.RestAdministrativeDivision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RestToBusinessAdministrativeDivisionMapper {

  private @Autowired RestToBusinessAdministrativeDivisionTypeMapper restToBusinessAdministrativeDivisionTypeMapper;

  public AdministrativeDivision map(RestAdministrativeDivision restAdministrativeDivision) {
    int id = restAdministrativeDivision.getId();
    AdministrativeDivisionType administrativeDivisionType = restToBusinessAdministrativeDivisionTypeMapper.map(restAdministrativeDivision.getType());
    String name = restAdministrativeDivision.getName();
    return new AdministrativeDivision(id, administrativeDivisionType, name);
  }
}
