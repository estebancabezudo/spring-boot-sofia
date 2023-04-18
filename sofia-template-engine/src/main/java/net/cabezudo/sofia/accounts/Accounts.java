package net.cabezudo.sofia.accounts;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Accounts {
  private final List<Account> list = new ArrayList();
  private final Set<Account> set = new TreeSet();

  public Account getFirst() {
    return list.get(0);
  }

  public boolean contains(Account account) {
    return set.contains(account);
  }

  public boolean add(Account account) {
    if (set.add(account)) {
      return list.add(account);
    }
    return false;
  }
}
