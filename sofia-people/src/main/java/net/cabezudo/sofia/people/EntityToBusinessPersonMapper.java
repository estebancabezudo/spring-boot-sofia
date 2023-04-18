package net.cabezudo.sofia.people;

public class EntityToBusinessPersonMapper {
  public Person map(PersonEntity p) {
    return new Person(p.getId(), p.getName(), p.getSecondName(), p.getLastName(), p.getSecondLastName(), p.getDateOfBirth());
  }
}
