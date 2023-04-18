package net.cabezudo.sofia.countries;

public class Country {
  private final int id;
  private final String code;

  public Country(int id, String code) {
    this.id = id;
    this.code = code;
  }

  public String getCode() {
    return code;
  }

  public int getId() {
    return id;
  }
}

