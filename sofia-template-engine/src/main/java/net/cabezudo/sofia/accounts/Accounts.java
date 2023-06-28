package net.cabezudo.sofia.accounts;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;

public class Accounts implements Iterable<Account> {
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

  @NotNull
  @Override
  public Iterator<Account> iterator() {
    return list.iterator();
  }

  @Override
  public void forEach(Consumer<? super Account> action) {
    Iterable.super.forEach(action);
  }
}
