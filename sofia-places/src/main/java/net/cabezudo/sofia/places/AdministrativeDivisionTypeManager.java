package net.cabezudo.sofia.places;

import org.springframework.stereotype.Service;

@Service
public class AdministrativeDivisionTypeManager {
  public AdministrativeDivisionType get(String typeName) {
    return new AdministrativeDivisionType(null, typeName);
  }
}
