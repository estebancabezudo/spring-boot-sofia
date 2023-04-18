package net.cabezudo.sofia.users.mappers;

import net.cabezudo.sofia.users.SofiaUser;
import net.cabezudo.sofia.users.persistence.UserEntity;
import net.cabezudo.sofia.users.rest.BusinessToRestGroupsMapper;
import org.springframework.stereotype.Component;

@Component
public class BusinessToEntityUserMapper {

  public UserEntity map(SofiaUser u) {
    BusinessToRestGroupsMapper businessToRestGroupsMapper = new BusinessToRestGroupsMapper();
    return new UserEntity(u.getId(), u.getSite(), u.getUsername(), u.getPassword(), businessToRestGroupsMapper.map(u.getGroups()), u.isEnabled());
  }
}
