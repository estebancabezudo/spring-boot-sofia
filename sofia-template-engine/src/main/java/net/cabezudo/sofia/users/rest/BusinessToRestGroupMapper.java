package net.cabezudo.sofia.users.rest;

import net.cabezudo.sofia.users.Group;

public class BusinessToRestGroupMapper {
  public RestGroup map(Group g) {
    return new RestGroup(g.name());
  }
}
