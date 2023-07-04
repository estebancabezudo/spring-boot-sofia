package net.cabezudo.sofia.users.mappers;

import net.cabezudo.sofia.users.service.Group;
import net.cabezudo.sofia.users.rest.RestGroup;
import org.springframework.stereotype.Component;

@Component
public class RestToBusinessGroupMapper {
  public Group map(RestGroup g) {
    return new Group(g.getName());
  }
}
