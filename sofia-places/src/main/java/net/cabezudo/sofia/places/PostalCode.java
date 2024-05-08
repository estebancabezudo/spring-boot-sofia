package net.cabezudo.sofia.places;

import lombok.Getter;

public class PostalCode {
  @Getter
  private final String code;
  @Getter
  private final boolean verified;

  public PostalCode(String code, boolean verified) {
    this.code = code;
    this.verified = verified;
  }
}
