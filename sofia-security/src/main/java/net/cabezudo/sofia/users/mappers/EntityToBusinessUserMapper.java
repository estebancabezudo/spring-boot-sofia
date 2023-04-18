package net.cabezudo.sofia.users.mappers;

import net.cabezudo.sofia.users.Groups;
import net.cabezudo.sofia.users.SofiaUser;
import net.cabezudo.sofia.users.persistence.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class EntityToBusinessUserMapper {
  public SofiaUser map(UserEntity e) {
    EntityToBusinessGroupsMapper mapper = new EntityToBusinessGroupsMapper();
    int id = e.getId();
    String username = e.getUsername();
    String password = e.getPassword();
    Groups groups = mapper.map(e.getEntityGroups());
    boolean isEnabled = e.isEnabled();
    return new SofiaUser(id, username, password, groups, isEnabled);
  }
}
