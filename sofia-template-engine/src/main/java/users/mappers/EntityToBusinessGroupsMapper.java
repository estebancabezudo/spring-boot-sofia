package users.mappers;

import net.cabezudo.sofia.users.Group;
import net.cabezudo.sofia.users.Groups;
import users.persistence.GroupEntity;
import users.persistence.GroupsEntity;

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
