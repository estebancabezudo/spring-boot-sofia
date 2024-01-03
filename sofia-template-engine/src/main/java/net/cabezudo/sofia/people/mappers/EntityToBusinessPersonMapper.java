package net.cabezudo.sofia.people.mappers;

import net.cabezudo.sofia.people.Person;
import net.cabezudo.sofia.people.persistence.PersonEntity;

import org.springframework.stereotype.Component;

@Component
public class EntityToBusinessPersonMapper {
  public Person map(PersonEntity p) {
    return new Person(p.getId(), p.getName(), p.getSecondName(), p.getLastName(), p.getSecondLastName(), p.getDateOfBirth());
  }
}
