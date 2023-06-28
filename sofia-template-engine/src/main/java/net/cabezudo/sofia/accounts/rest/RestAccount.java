package net.cabezudo.sofia.accounts.rest;

public class RestAccount {
  private final int id;
  private final String name;

  public RestAccount(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}
