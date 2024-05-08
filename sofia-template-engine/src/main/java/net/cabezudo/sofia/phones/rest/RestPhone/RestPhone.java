package net.cabezudo.sofia.phones.rest.RestPhone;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RestPhone {
  private int countryCode;
  private long number;

  public RestPhone(int countryCode, long number) {
    this.countryCode = countryCode;
    this.number = number;
  }
}
