package net.cabezudo.sofia.web.client.mappers;

import net.cabezudo.sofia.accounts.Account;
import net.cabezudo.sofia.accounts.mappers.BusinessToEntityAccountMapper;
import net.cabezudo.sofia.accounts.persistence.AccountEntity;
import net.cabezudo.sofia.web.client.WebClientData;
import net.cabezudo.sofia.web.client.repository.WebClientDataEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BusinessToEntityWebClientDataMapper {
  private @Autowired
  BusinessToEntityAccountMapper businessToEntityAccountMapper;

  public WebClientDataEntity map(WebClientData data) {
    Account account = data.getAccount();
    String languageCode = data.getLanguage() == null ? null : data.getLanguage().getCode();
    AccountEntity accountEntity = businessToEntityAccountMapper.map(account);
    return new WebClientDataEntity(data.getId(), languageCode, accountEntity, data.getLastUpdate());
  }
}
