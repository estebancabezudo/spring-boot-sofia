package net.cabezudo.sofia.users.mappers;

import net.cabezudo.sofia.users.SofiaUser;
import net.cabezudo.sofia.users.mappers.BusinessToRestGroupsMapper;
import net.cabezudo.sofia.users.rest.RestUser;
import org.springframework.stereotype.Component;

@Component
public class BusinessToRestUserMapper {

  public RestUser map(SofiaUser u) {
    BusinessToRestGroupsMapper businessToRestGroupsMapper = new BusinessToRestGroupsMapper();
    return new RestUser(u.getId(), u.getAccount(), u.getUsername(), u.getPassword(), businessToRestGroupsMapper.map(u.getGroups()), u.getLocale().toString(), u.isEnabled());
  }
}
