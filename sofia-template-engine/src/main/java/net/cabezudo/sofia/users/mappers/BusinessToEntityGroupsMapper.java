package net.cabezudo.sofia.users.mappers;

import net.cabezudo.sofia.users.Group;
import net.cabezudo.sofia.users.Groups;
import net.cabezudo.sofia.users.persistence.GroupEntity;
import net.cabezudo.sofia.users.persistence.GroupsEntity;
import org.springframework.stereotype.Component;

@Component
public class BusinessToEntityGroupsMapper {

  public GroupsEntity map(int accountUserRelationId, Groups groups) {
    GroupsEntity groupsEntity = new GroupsEntity();
    for (Group group : groups) {
      GroupEntity groupEntity = new GroupEntity(accountUserRelationId, group.getName());
      groupsEntity.add(groupEntity);
    }
    return groupsEntity;
  }
}
