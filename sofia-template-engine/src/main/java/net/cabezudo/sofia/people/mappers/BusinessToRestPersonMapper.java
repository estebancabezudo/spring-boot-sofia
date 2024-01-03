package net.cabezudo.sofia.people.mappers;

import net.cabezudo.sofia.people.Person;
import net.cabezudo.sofia.people.rest.RestPerson;


import java.util.Date;

public class BusinessToRestPersonMapper {
  public RestPerson map(Person p) {

    int id = p.getId();
    String name = p.getName();
    String secondName = p.getSecondName();
    String lastName = p.getLastName();
    String secondLastName = p.getSecondLastName();
    Date dateOfBirth = p.getDateOfBirth();

    return new RestPerson(id, name, secondName, lastName, secondLastName, dateOfBirth);
  }
}
