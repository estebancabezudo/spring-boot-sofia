package net.cabezudo.sofia.users.mappers;

import net.cabezudo.sofia.users.Group;
import net.cabezudo.sofia.users.Groups;
import net.cabezudo.sofia.users.rest.RestGroup;
import net.cabezudo.sofia.users.rest.RestGroups;

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
