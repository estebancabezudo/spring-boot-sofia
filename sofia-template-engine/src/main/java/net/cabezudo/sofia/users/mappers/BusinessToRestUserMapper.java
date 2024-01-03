package net.cabezudo.sofia.users.mappers;

import net.cabezudo.sofia.users.rest.RestSofiaUser;
import net.cabezudo.sofia.users.service.SofiaUser;
import org.springframework.stereotype.Component;

@Component
public class BusinessToRestUserMapper {

  public RestSofiaUser map(SofiaUser u) {
    if (u == null) {
      return null;
    }
    BusinessToRestGroupsMapper businessToRestGroupsMapper = new BusinessToRestGroupsMapper();
    return new RestSofiaUser(u.getId(), u.getAccount(), u.getUsername(), u.getPassword(), businessToRestGroupsMapper.map(u.getGroups()), u.getLocale().toString(), u.isEnabled());
  }
}
