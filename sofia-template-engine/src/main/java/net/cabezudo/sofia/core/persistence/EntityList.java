package net.cabezudo.sofia.core.persistence;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class EntityList<T> implements Iterable<T> {
  private final List<T> data;
  private int total;
  private int start;
  private int size;

  public EntityList() {
    this.data = new ArrayList<>();
  }

  public EntityList(int total, int start, int size) {
    this.data = new ArrayList<>();
    this.total = total;
    this.start = start;
    this.size = size;
  }

  public EntityList(int total, int start, int size, List<T> data) {
    this.data = data;
    this.total = total;
    this.start = start;
    this.size = size;
  }

  @Override
  public Iterator<T> iterator() {
    return data.iterator();
  }

  @Override
  public void forEach(Consumer<? super T> action) {
    Iterable.super.forEach(action);
  }

  public boolean add(T t) {
    return data.add(t);
  }

  public int getTotal() {
    return total;
  }

  public void setTotal(int total) {
    this.total = total;
  }

  public int getSize() {
    return size;
  }

  public int getStart() {
    return start;
  }

  public int size() {
    return data.size();
  }

  public List<T> getData() {
    return data;
  }
}
