package net.cabezudo.sofia.users.rest;

import net.cabezudo.sofia.users.SofiaUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RestToBusinessUserMapper {

  @Autowired
  RestToBusinessGroupsMapper restToBusinessGroupsMapper;

  public SofiaUser map(RestUser u) {
    return new SofiaUser(u.getId(), u.getUsername(), null, restToBusinessGroupsMapper.map(u.getGroups()), u.isEnabled());
  }
}
