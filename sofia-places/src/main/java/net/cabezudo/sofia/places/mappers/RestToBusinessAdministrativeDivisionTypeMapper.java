package net.cabezudo.sofia.places.mappers;

import net.cabezudo.sofia.places.AdministrativeDivisionType;
import net.cabezudo.sofia.places.AdministrativeDivisionTypeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class RestToBusinessAdministrativeDivisionTypeMapper {

  private @Autowired AdministrativeDivisionTypeManager administrativeDivisionTypeManager;

  public AdministrativeDivisionType map(String typeName) {
    return administrativeDivisionTypeManager.get(typeName);
  }
}
