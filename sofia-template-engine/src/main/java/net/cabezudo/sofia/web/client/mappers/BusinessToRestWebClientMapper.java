package net.cabezudo.sofia.web.client.mappers;

import net.cabezudo.sofia.accounts.Account;
import net.cabezudo.sofia.users.SofiaUser;
import net.cabezudo.sofia.users.mappers.BusinessToRestUserMapper;
import net.cabezudo.sofia.users.rest.RestUser;
import net.cabezudo.sofia.web.client.WebClientData;
import net.cabezudo.sofia.web.client.rest.RestWebClientData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BusinessToRestWebClientMapper {
  private @Autowired BusinessToRestUserMapper businessToRestUserMapper;

  public RestWebClientData map(WebClientData c) {
    String language = c == null || c.getLanguage() == null ? null : c.getLanguage().getCode();
    Account account = c == null ? null : c.getAccount();
    SofiaUser user = c == null ? null : c.getUser();
    RestUser restUser = user == null ? null : businessToRestUserMapper.map(user);
    return new RestWebClientData(language, account, restUser, c.getMessage());
  }
}
