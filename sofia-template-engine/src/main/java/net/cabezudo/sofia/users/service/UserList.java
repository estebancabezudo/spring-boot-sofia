package net.cabezudo.sofia.users.service;

import net.cabezudo.sofia.core.service.BusinessList;
import net.cabezudo.sofia.users.SofiaUser;

public class UserList extends BusinessList<SofiaUser> {
  public UserList(int total, int start, int size) {
    super(total, start, size);
  }
}
