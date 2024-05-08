package net.cabezudo.sofia.places.mappers;

import net.cabezudo.sofia.places.AdministrativeDivision;
import net.cabezudo.sofia.places.rest.RestAdministrativeDivision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BusinessToRestAdministrativeDivisionMapper {
  private @Autowired BusinessToRestAdministrativeDivisionTypeMapper businessToRestAdministrativeDivisionTypeMapper;

  public RestAdministrativeDivision map(AdministrativeDivision administrativeDivision) {
    int id = administrativeDivision.getId();
    String type = businessToRestAdministrativeDivisionTypeMapper.map(administrativeDivision.getType());
    String name = administrativeDivision.getName();
    return new RestAdministrativeDivision(id, type, name);
  }
}
