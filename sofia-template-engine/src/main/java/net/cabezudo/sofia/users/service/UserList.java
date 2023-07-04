package net.cabezudo.sofia.users.service;

import net.cabezudo.sofia.core.service.BusinessList;

public class UserList extends BusinessList<SofiaUser> {
  public UserList(int total, int start, int size) {
    super(total, start, size);
  }
}
