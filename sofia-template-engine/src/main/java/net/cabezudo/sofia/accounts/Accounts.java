package net.cabezudo.sofia.accounts;

import net.cabezudo.sofia.core.service.BusinessList;

public class Accounts extends BusinessList<Account> {
  public Accounts(int total, int start, int end) {
    super(total, start, end);
  }
}
