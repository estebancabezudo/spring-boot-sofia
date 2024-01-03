package net.cabezudo.sofia.users.mappers;

import net.cabezudo.sofia.users.service.Group;
import net.cabezudo.sofia.users.service.Groups;
import net.cabezudo.sofia.users.rest.RestGroup;
import net.cabezudo.sofia.users.rest.RestGroups;

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
