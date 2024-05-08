package net.cabezudo.sofia.countries;

import lombok.Getter;

import java.util.Objects;

@Getter
public final class Country implements Comparable<Country> {
  private final int id;
  private final String code;
  private final int phoneCode;
  private final String phoneFormat;

  public Country(int id, String code, int phoneCode, String phoneFormat) {
    this.id = id;
    this.code = code;
    this.phoneCode = phoneCode;
    this.phoneFormat = phoneFormat;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Country country)) return false;
    return Objects.equals(getCode(), country.getCode());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getCode());
  }

  @Override
  public String toString() {
    return "Country[" +
        "id=" + id + ", " +
        "code=" + code + ", " +
        "phoneCode=" + phoneCode + ", " +
        "phoneFormat=" + phoneFormat + ']';
  }

  @Override
  public int compareTo(Country o) {
    return this.code.compareTo(o.code);
  }
}

