package net.cabezudo.sofia.users.service;

import net.cabezudo.sofia.core.service.BusinessList;

public class SofiaUserList extends BusinessList<SofiaUser> {
  public SofiaUserList(int total, int start, int size) {
    super(total, start, size);
  }
}
