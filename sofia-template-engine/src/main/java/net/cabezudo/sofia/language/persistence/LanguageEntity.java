package net.cabezudo.sofia.language.persistence;

import jakarta.persistence.Entity;
import lombok.Getter;

@Entity
@Getter
public class LanguageEntity {
  private final int id;
  private final String code;

  public LanguageEntity(int id, String code) {
    this.id = id;
    this.code = code;
  }
}
