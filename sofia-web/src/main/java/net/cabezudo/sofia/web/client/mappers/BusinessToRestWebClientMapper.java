package net.cabezudo.sofia.web.client.mappers;

import net.cabezudo.sofia.users.SofiaUser;
import net.cabezudo.sofia.users.rest.BusinessToRestUserMapper;
import net.cabezudo.sofia.users.rest.RestUser;
import net.cabezudo.sofia.web.client.WebClientData;
import net.cabezudo.sofia.web.client.rest.RestWebClientData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BusinessToRestWebClientMapper {
  private @Autowired BusinessToRestUserMapper businessToRestUserMapper;

  public RestWebClientData map(WebClientData c) {
    String language = c == null || c.getLanguage() == null ? null : c.getLanguage().getValue();
    SofiaUser user = c.getUser();
    RestUser restUser = user == null ? null : businessToRestUserMapper.map(user);
    return new RestWebClientData(language, c.getAccount(), restUser);
  }
}
