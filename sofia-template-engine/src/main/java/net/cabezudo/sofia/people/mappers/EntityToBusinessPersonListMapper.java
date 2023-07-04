package net.cabezudo.sofia.people.mappers;

import net.cabezudo.sofia.core.persistence.EntityList;
import net.cabezudo.sofia.people.PeopleList;
import net.cabezudo.sofia.people.persistence.PersonEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EntityToBusinessPersonListMapper {
  private @Autowired EntityToBusinessPersonMapper entityToBusinessPersonMapper;

  public PeopleList map(EntityList<PersonEntity> list) {
    
    int total = list.getTotal();
    int start = list.getStart();
    int size = list.getSize();
    PeopleList peopleList = new PeopleList(total, start, size);

    for (PersonEntity personEntity : list) {
      peopleList.add(entityToBusinessPersonMapper.map(personEntity));
    }
    return peopleList;
  }
}
