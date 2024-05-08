package net.cabezudo.sofia.web.client.mappers;

import net.cabezudo.sofia.accounts.mappers.EntityToBusinessAccountMapper;
import net.cabezudo.sofia.accounts.service.Account;
import net.cabezudo.sofia.language.Language;
import net.cabezudo.sofia.web.client.WebClientData;
import net.cabezudo.sofia.web.client.persistence.WebClientDataEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EntityToBusinessWebClientDataMapper {

  private @Autowired EntityToBusinessAccountMapper entityToBusinessAccountMapper;

  public WebClientData map(WebClientDataEntity data) {
    if (data == null) {
      return null;
    }
    Language language = data.getLanguage() == null ? null : new Language(data.getLanguage());
    Account account = entityToBusinessAccountMapper.map(data.getAccount());
    return new WebClientData(data.getId(), language, account, data.getLastUpdate());
  }
}
