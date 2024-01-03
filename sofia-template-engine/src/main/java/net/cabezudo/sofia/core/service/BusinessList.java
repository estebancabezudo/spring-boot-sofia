package net.cabezudo.sofia.core.service;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BusinessList<T> implements Iterable<T> {
  private final int total;
  private final int start;
  private final int size;
  private final List<T> list;

  public BusinessList(int total, int start, int size) {
    this(total, start, size, new ArrayList<>());
  }

  public BusinessList(int total, int start, int size, List<T> list) {
    this.total = total;
    this.start = start;
    this.size = size;
    this.list = list;
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

  @Override
  public Iterator<T> iterator() {
    return list.iterator();
  }

  public boolean add(T o) {
    return list.add(o);
  }
}
