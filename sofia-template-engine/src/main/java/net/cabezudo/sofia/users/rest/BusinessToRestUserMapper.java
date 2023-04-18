package net.cabezudo.sofia.users.rest;

import net.cabezudo.sofia.users.SofiaUser;
import org.springframework.stereotype.Component;

@Component
public class BusinessToRestUserMapper {

  public RestUser map(SofiaUser u) {
    BusinessToRestGroupsMapper businessToRestGroupsMapper = new BusinessToRestGroupsMapper();
    return new RestUser(u.getId(), u.getUsername(), businessToRestGroupsMapper.map(u.getGroups()), u.isEnabled());
  }
}
