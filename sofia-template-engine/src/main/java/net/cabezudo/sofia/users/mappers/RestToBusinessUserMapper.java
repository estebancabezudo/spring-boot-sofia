package net.cabezudo.sofia.users.mappers;

import net.cabezudo.sofia.users.SofiaUser;
import net.cabezudo.sofia.users.rest.RestUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class RestToBusinessUserMapper {

  private @Autowired RestToBusinessGroupsMapper restToBusinessGroupsMapper;

  public SofiaUser map(RestUser u) {
    return new SofiaUser(u.getId(), u.getAccount(), u.getUsername(), u.getPassword(), restToBusinessGroupsMapper.map(u.getGroups()), new Locale(u.getLocale()), u.isEnabled());
  }
}
