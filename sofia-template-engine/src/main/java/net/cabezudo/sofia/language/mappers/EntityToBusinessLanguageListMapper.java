package net.cabezudo.sofia.language.mappers;

import net.cabezudo.sofia.language.Language;
import net.cabezudo.sofia.language.LanguageList;
import net.cabezudo.sofia.language.persistence.LanguageEntity;
import net.cabezudo.sofia.language.persistence.LanguageEntityList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EntityToBusinessLanguageListMapper {
  private @Autowired EntityToBusinessLanguageMapper entityToBusinessLanguageMapper;

  public LanguageList map(LanguageEntityList languageEntityList) {
    LanguageList languageList = new LanguageList();
    if (languageEntityList != null) {
      for (LanguageEntity languageEntity : languageEntityList) {
        Language language = entityToBusinessLanguageMapper.map(languageEntity);
        languageList.add(language);
      }
    }
    return languageList;
  }
}
