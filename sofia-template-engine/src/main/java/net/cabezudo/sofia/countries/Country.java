package net.cabezudo.sofia.countries;


import java.util.Objects;

public final class Country {
  private final int id;
  private final String code;

  public Country(int id, String code) {
    this.id = id;
    this.code = code;
  }

  public int id() {
    return id;
  }

  public String code() {
    return code;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (Country) obj;
    return this.id == that.id &&
        Objects.equals(this.code, that.code);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, code);
  }

  @Override
  public String toString() {
    return "Country[" +
        "id=" + id + ", " +
        "code=" + code + ']';
  }

}

