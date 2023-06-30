package net.cabezudo.sofia.people;

import net.cabezudo.sofia.people.persistence.PersonEntity;

import java.util.Date;

public class Person {
  private final int id;
  private final String name;
  private final String secondName;
  private final String lastName;
  private final String secondLastName;
  private final Date dateOfBirth;

  public Person(int id, String name, String secondName, String lastName, String secondLlastName, Date dateOfBirth) {
    this.id = id;
    this.name = name;
    this.secondName = secondName;
    this.lastName = lastName;
    this.secondLastName = secondLlastName;
    this.dateOfBirth = dateOfBirth;
  }

  public Person(PersonEntity entity) {
    this.id = entity.getId();
    this.name = entity.getName();
    this.secondName = entity.getSecondName();
    this.lastName = entity.getLastName();
    this.secondLastName = entity.getSecondLastName();
    this.dateOfBirth = entity.getDateOfBirth();
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getSecondName() {
    return secondName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getSecondLastName() {
    return secondLastName;
  }

  public Date getDateOfBirth() {
    return dateOfBirth;
  }
}
