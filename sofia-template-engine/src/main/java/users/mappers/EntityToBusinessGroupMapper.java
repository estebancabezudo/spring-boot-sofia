package users.mappers;

import net.cabezudo.sofia.users.Group;
import users.persistence.GroupEntity;

public class EntityToBusinessGroupMapper {
  public Group map(GroupEntity entity) {
    return new Group(entity.getName());
  }
}
