package net.cabezudo.sofia.places;

import java.util.Objects;

public record AdministrativeDivision(Integer id, AdministrativeDivisionType administrativeDivisionType, String name) {
  public AdministrativeDivision {
    Objects.requireNonNull(administrativeDivisionType);
    Objects.requireNonNull(name);
  }
}
