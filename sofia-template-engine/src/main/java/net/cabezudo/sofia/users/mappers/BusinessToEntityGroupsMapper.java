package net.cabezudo.sofia.users.mappers;

import net.cabezudo.sofia.users.Group;
import net.cabezudo.sofia.users.Groups;
import net.cabezudo.sofia.users.persistence.GroupEntity;
import net.cabezudo.sofia.users.persistence.GroupsEntity;
import net.cabezudo.sofia.users.persistence.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BusinessToEntityGroupsMapper {

  private @Autowired BusinessToEntityGroupsMapper businessToEntityGroupsMapper;

  public GroupsEntity map(UserEntity user, Groups groups) {
    GroupsEntity groupsEntity = new GroupsEntity();
    for (Group group : groups) {
      GroupEntity groupEntity = new GroupEntity(user.getId(), group.name());
      groupsEntity.add(groupEntity);
    }
    return groupsEntity;
  }
}
