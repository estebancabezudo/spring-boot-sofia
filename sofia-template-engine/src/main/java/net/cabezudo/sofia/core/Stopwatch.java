package net.cabezudo.sofia.core;


public class Stopwatch {
  private Long start;
  private Long end;

  public Stopwatch start() {
    start = System.currentTimeMillis();
    return this;
  }

  public Stopwatch stop() {
    end = System.currentTimeMillis();
    return this;
  }

  public Long getValue() {
    if (end == null) {
      return null;
    }
    return end - start;
  }
}
