package net.cabezudo.sofia.core.rest;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class RestList<R> implements Iterable<R> {

  private final List<R> list = new ArrayList<>();
  private int total;
  private int start;
  private int size;

  public RestList() {
    start = 0;
  }

  public RestList(R[] values) {
    list.addAll(Arrays.asList(values));
    total = list.size();
    size = total;
  }

  public List<R> getList() {
    return list;
  }

  public int getTotal() {
    return total;
  }

  public int getStart() {
    return start;
  }

  public int getSize() {
    return size;
  }

  public boolean add(R o) {
    total++;
    size++;
    return list.add(o);
  }

  @NotNull
  @Override
  public Iterator<R> iterator() {
    return list.iterator();
  }
}
