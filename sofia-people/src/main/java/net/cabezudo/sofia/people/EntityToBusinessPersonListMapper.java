package net.cabezudo.sofia.people;

import net.cabezudo.sofia.core.persistence.EntityList;

public class EntityToBusinessPersonListMapper {

  public PeopleList map(EntityList<PersonEntity> list) {
    EntityToBusinessPersonMapper mapper = new EntityToBusinessPersonMapper();

    int total = list.getTotal();
    int start = list.getStart();
    int size = list.getSize();
    PeopleList peopleList = new PeopleList(total, start, size);

    for (PersonEntity personEntity : list) {
      peopleList.add(mapper.map(personEntity));
    }
    return peopleList;
  }


}
