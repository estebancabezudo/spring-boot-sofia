package net.cabezudo.sofia.phones;

import lombok.Getter;
import net.cabezudo.sofia.core.InvalidParameterException;

import java.util.Objects;

public class Phone implements Comparable<Phone> {
  @Getter
  private final int countryCode;
  @Getter
  private final Long number;

  public Phone(Integer countryCode, Long number) {
    this.countryCode = countryCode;
    this.number = number;
    if (countryCode == null) {
      throw new InvalidParameterException("The country code cant be null");
    }
    if (number == null) {
      throw new InvalidParameterException("The number cant be null");
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Phone phone)) return false;
    return Objects.equals(countryCode, phone.countryCode) && Objects.equals(number, phone.number);
  }

  @Override
  public int hashCode() {
    return Objects.hash(countryCode, number);
  }

  @Override
  public int compareTo(Phone o) {
    int c = Integer.compare(this.countryCode, o.countryCode);
    if (c != 0) {
      return c;
    } else {
      return this.number.compareTo(o.number);
    }
  }
}
