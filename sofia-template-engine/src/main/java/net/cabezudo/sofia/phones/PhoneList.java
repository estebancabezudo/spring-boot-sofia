package net.cabezudo.sofia.phones;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;

public class PhoneList implements Iterable<Phone> {
  private final Set<Phone> set = new TreeSet<>();

  public void add(Phone phone) {
    set.add(phone);
  }

  @NotNull
  @Override
  public Iterator<Phone> iterator() {
    return set.iterator();
  }

  @Override
  public void forEach(Consumer<? super Phone> action) {
    Iterable.super.forEach(action);
  }
}
