package net.cabezudo.sofia.users.mappers;

import net.cabezudo.sofia.users.service.Group;
import net.cabezudo.sofia.users.rest.RestGroup;

public class BusinessToRestGroupMapper {
  public RestGroup map(Group g) {
    return new RestGroup(g.getName());
  }
}
