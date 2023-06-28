package net.cabezudo.sofia.people.mappers;

import net.cabezudo.sofia.people.PeopleList;
import net.cabezudo.sofia.people.Person;
import net.cabezudo.sofia.people.RestPersonList;

public class BusinessToRestPersonListMapper {

  public RestPersonList map(PeopleList list) {
    BusinessToRestPersonMapper mapper = new BusinessToRestPersonMapper();

    int total = list.getTotal();
    int start = list.getStart();
    int size = list.getSize();
    RestPersonList restList = new RestPersonList();

    for (Person person : list) {
      restList.add(mapper.map(person));
    }
    return restList;
  }
}
