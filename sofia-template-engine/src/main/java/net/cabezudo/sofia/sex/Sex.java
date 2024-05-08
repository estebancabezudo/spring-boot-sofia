package net.cabezudo.sofia.sex;

public enum Sex {
  MALE, FEMALE;

  public Character toCharacter() {
    return name().charAt(0);
  }
}
