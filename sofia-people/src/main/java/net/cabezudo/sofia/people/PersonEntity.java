package net.cabezudo.sofia.people;

import java.util.Date;

public class PersonEntity {
  private int id;
  private String name;
  private String secondName;
  private String lastName;
  private String secondLlastName;
  private Date dateOfBirth;

  public PersonEntity(int id, String name, String secondName, String lastName, String secondLlastName, Date dateOfBirth) {
    this.id = id;
    this.name = name;
    this.secondName = secondName;
    this.lastName = lastName;
    this.secondLlastName = secondLlastName;
    this.dateOfBirth = dateOfBirth;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSecondName() {
    return secondName;
  }

  public void setSecondName(String secondName) {
    this.secondName = secondName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getSecondLastName() {
    return secondLlastName;
  }

  public void setSecondLlastName(String secondLlastName) {
    this.secondLlastName = secondLlastName;
  }

  public Date getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(Date dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

}
