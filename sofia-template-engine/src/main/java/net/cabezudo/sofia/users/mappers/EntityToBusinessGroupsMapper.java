package net.cabezudo.sofia.users.mappers;

import net.cabezudo.sofia.users.service.Group;
import net.cabezudo.sofia.users.service.Groups;
import net.cabezudo.sofia.users.persistence.GroupEntity;
import net.cabezudo.sofia.users.persistence.GroupsEntity;


public class EntityToBusinessGroupsMapper {
  public Groups map(GroupsEntity groupsEntity) {
    Groups groups = new Groups();
    EntityToBusinessGroupMapper mapper = new EntityToBusinessGroupMapper();
    for (GroupEntity e : groupsEntity) {
      Group group = mapper.map(e);
      groups.add(group);
    }
    return groups;
  }
}
