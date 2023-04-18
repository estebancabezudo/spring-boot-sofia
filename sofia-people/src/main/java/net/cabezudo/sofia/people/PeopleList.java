package net.cabezudo.sofia.people;

import net.cabezudo.sofia.core.service.BusinessList;

public class PeopleList extends BusinessList<Person> {
  public PeopleList(int total, int start, int end) {
    super(total, start, end);
  }
}
