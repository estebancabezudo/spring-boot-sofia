package net.cabezudo.sofia.web.client.mappers;

import net.cabezudo.sofia.accounts.service.Account;
import net.cabezudo.sofia.users.mappers.BusinessToRestUserMapper;
import net.cabezudo.sofia.users.rest.RestSofiaUser;
import net.cabezudo.sofia.web.client.WebClientData;
import net.cabezudo.sofia.web.client.rest.RestWebClientData;
import net.cabezudo.sofia.web.user.WebUserData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BusinessToRestWebClientMapper {
  private @Autowired BusinessToRestUserMapper businessToRestUserMapper;

  public RestWebClientData map(WebClientData c, WebUserData u) {
    String language = c == null || c.getLanguage() == null ? null : c.getLanguage().getCode();
    Account account = c == null ? null : c.getAccount();
    RestSofiaUser restSofiaUser = businessToRestUserMapper.map(u.getUser());
    return new RestWebClientData(language, account, restSofiaUser);
  }
}
