package net.cabezudo.sofia.users.service;

public class Password {
  private final String value;

  public Password(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return value;
  }
}
