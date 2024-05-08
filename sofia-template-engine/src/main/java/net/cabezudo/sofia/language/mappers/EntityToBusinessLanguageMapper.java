package net.cabezudo.sofia.language.mappers;

import net.cabezudo.sofia.language.Language;
import net.cabezudo.sofia.language.persistence.LanguageEntity;
import org.springframework.stereotype.Component;

@Component
public class EntityToBusinessLanguageMapper {
  public Language map(LanguageEntity languageEntity) {
    if (languageEntity == null) {
      return null;
    }
    return new Language(languageEntity.getCode());
  }
}
