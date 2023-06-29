package net.cabezudo.sofia.people;

import java.util.Date;
import java.util.Locale;

public class OAuthPersonData {
  private final String email;
  private final String name;
  private final String secondName;
  private final String lastName;
  private final String secondLlastName;
  private final Date dateOfBirth;
  private final Locale locale;

  public OAuthPersonData(String email, String name, String secondName, String lastName, String secondLlastName, Date dateOfBirth, Locale locale) {

    this.email = email;
    this.name = name;
    this.secondName = secondName;
    this.lastName = lastName;
    this.secondLlastName = secondLlastName;
    this.dateOfBirth = dateOfBirth;
    this.locale = locale;
  }

  public String getEmail() {
    return email;
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

  public String getSecondLlastName() {
    return secondLlastName;
  }

  public Date getDateOfBirth() {
    return dateOfBirth;
  }

  public Locale getLocale() {
    return locale;
  }
}
