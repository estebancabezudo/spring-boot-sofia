package net.cabezudo.sofia.users.mappers;

import net.cabezudo.sofia.accounts.mappers.BusinessToRestAccountMapper;
import net.cabezudo.sofia.accounts.rest.RestAccount;
import net.cabezudo.sofia.users.rest.RestSofiaUser;
import net.cabezudo.sofia.users.rest.RestWebUserData;
import net.cabezudo.sofia.web.client.rest.RestLanguage;
import net.cabezudo.sofia.web.user.WebUserData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BusinessToRestWebUserMapper {
  private @Autowired BusinessToRestAccountMapper businessToRestAccountMapper;
  private @Autowired BusinessToRestUserMapper businessToRestUserMapper;

  public RestWebUserData map(WebUserData data) {
    RestLanguage restLanguage = new RestLanguage(data.getLanguage().getCode());
    RestAccount restAccount = businessToRestAccountMapper.map(data.getAccount());
    RestSofiaUser restSofiaUser = businessToRestUserMapper.map(data.getUser());

    return new RestWebUserData(restLanguage, restAccount, restSofiaUser);
  }
}
