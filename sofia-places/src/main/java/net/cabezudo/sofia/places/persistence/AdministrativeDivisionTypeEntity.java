package net.cabezudo.sofia.places.persistence;

import jakarta.persistence.Entity;
import lombok.Getter;

@Entity
@Getter
public class AdministrativeDivisionTypeEntity {
  private final Integer id;
  private final String name;

  public AdministrativeDivisionTypeEntity(Integer id, String name) {
    this.id = id;
    this.name = name;
  }
}
