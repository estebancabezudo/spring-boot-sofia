package net.cabezudo.sofia.users.mappers;

import net.cabezudo.sofia.emails.persistence.EMailEntity;
import net.cabezudo.sofia.users.Groups;
import net.cabezudo.sofia.users.SofiaUser;
import net.cabezudo.sofia.users.persistence.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class EntityToBusinessUserMapper {
  public SofiaUser map(UserEntity e) {
    EntityToBusinessGroupsMapper mapper = new EntityToBusinessGroupsMapper();
    int id = e.getId();
    int siteId = e.getSiteId();
    EMailEntity eMailEntity = e.getEMailEntity();
    String password = e.getPassword();
    Groups groups = mapper.map(e.getEntityGroups());
    boolean isEnabled = e.isEnabled();
    return new SofiaUser(id, siteId, eMailEntity.email(), password, groups, isEnabled);
  }
}
