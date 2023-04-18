package net.cabezudo.sofia.users.rest;

import net.cabezudo.sofia.users.Group;
import net.cabezudo.sofia.users.Groups;

public class BusinessToRestGroupsMapper {
  public RestGroups map(Groups gs) {
    RestGroups groups = new RestGroups();
    BusinessToRestGroupMapper mapper = new BusinessToRestGroupMapper();
    for (Group g : gs) {
      RestGroup group = mapper.map(g);
      groups.add(group);
    }
    return groups;
  }
}
