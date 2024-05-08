package net.cabezudo.sofia.places.persistence;

import jakarta.persistence.Entity;
import lombok.Getter;

@Entity
@Getter
public class PostalCodeEntity {
  public final int id;
  public final String code;
  public final boolean verified;

  public PostalCodeEntity(int id, String code, boolean verified) {
    this.id = id;
    this.code = code;
    this.verified = verified;
  }
}
