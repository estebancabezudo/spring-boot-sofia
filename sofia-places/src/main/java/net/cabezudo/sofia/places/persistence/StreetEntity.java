package net.cabezudo.sofia.places.persistence;

import jakarta.persistence.Entity;
import lombok.Getter;

@Entity
@Getter
public class StreetEntity {
  private final int id;
  private final String name;
  private final boolean verified;

  public StreetEntity(int id, String name, boolean verified) {
    this.id = id;
    this.name = name;
    this.verified = verified;
  }
}
