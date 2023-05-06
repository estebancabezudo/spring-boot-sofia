package net.cabezudo.sofia.users.rest;

import net.cabezudo.sofia.users.Group;
import net.cabezudo.sofia.users.Groups;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RestToBusinessGroupsMapper {

  private @Autowired RestToBusinessGroupMapper restToBusinessGroupMapper;

  public Groups map(RestGroups gs) {
    Groups groups = new Groups();

    for (RestGroup g : gs) {
      Group group = restToBusinessGroupMapper.map(g);
      groups.add(group);
    }
    return groups;
  }
}
