package net.cabezudo.sofia.places.mappers;

import net.cabezudo.sofia.places.AdministrativeDivisionType;
import org.springframework.stereotype.Component;

@Component
public class BusinessToRestAdministrativeDivisionTypeMapper {
  public String map(AdministrativeDivisionType administrativeDivisionType) {
    return administrativeDivisionType.getName();
  }
}
