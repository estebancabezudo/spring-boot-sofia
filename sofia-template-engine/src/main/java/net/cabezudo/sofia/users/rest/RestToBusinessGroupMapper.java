package net.cabezudo.sofia.users.rest;

import net.cabezudo.sofia.users.Group;
import org.springframework.stereotype.Component;

@Component
public class RestToBusinessGroupMapper {
  public Group map(RestGroup g) {
    return new Group(g.name());
  }
}
