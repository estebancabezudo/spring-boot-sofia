package net.cabezudo.sofia.places.mappers;

import net.cabezudo.sofia.places.AdministrativeDivision;
import net.cabezudo.sofia.places.rest.RestAdministrativeDivision;
import net.cabezudo.sofia.places.rest.RestAdministrativeDivisionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BusinessToRestAdminitrativeDivisionMapper {
  private @Autowired BusinessToRestAdminitrativeDivisionTypeMapper businessToRestAdminitrativeDivisionTypeMapper;

  public RestAdministrativeDivision map(AdministrativeDivision administrativeDivision) {
    int id = administrativeDivision.getId();

    RestAdministrativeDivisionType type = businessToRestAdminitrativeDivisionTypeMapper.map(administrativeDivision.getAdministrativeDivisionType());
    String name = administrativeDivision.getName();
    return new RestAdministrativeDivision(id, type, name);
  }
}
