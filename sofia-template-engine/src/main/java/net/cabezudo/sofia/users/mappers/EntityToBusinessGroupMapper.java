package net.cabezudo.sofia.users.mappers;

import net.cabezudo.sofia.users.service.Group;
import net.cabezudo.sofia.users.persistence.GroupEntity;


public class EntityToBusinessGroupMapper {
  public Group map(GroupEntity entity) {
    return new Group(entity.getName());
  }
}
