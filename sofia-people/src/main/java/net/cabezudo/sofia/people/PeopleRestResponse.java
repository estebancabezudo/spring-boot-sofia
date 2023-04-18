package net.cabezudo.sofia.people;

import net.cabezudo.sofia.core.rest.SofiaRestResponse;

public class PeopleRestResponse extends SofiaRestResponse<RestPerson> {
  public PeopleRestResponse(int status, String message, RestPerson person) {
    super(status, message, person);
  }
}
