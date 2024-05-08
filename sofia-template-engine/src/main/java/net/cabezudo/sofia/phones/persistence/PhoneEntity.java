package net.cabezudo.sofia.phones.persistence;

import jakarta.persistence.Entity;
import lombok.Getter;

@Entity
@Getter
public class PhoneEntity {
  private final int id;
  private final int countryCode;
  private final long number;

  public PhoneEntity(int id, int countryCode, long number) {
    this.id = id;
    this.countryCode = countryCode;
    this.number = number;
  }
}
