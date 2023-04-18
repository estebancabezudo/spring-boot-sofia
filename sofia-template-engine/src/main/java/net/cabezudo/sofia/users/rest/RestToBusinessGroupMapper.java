package net.cabezudo.sofia.users.rest;

import net.cabezudo.sofia.users.Group;

public class RestToBusinessGroupMapper {
  public Group map(RestGroup g) {
    return new Group(g.name());
  }
}
