package net.cabezudo.sofia.people;

import java.util.Date;

public class RestPerson {
  private final int id;
  private final String name;
  private final String secondName;
  private final String lastName;
  private final String secondLastName;
  private final Date dateOfBirth;

  public RestPerson(int id, String name, String secondName, String lastName, String secondLastName, Date dateOfBirth) {
    this.id = id;
    this.name = name;
    this.secondName = secondName;
    this.lastName = lastName;
    this.secondLastName = secondLastName;
    this.dateOfBirth = dateOfBirth;
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
