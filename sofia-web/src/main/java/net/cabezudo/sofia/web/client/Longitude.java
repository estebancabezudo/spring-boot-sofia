package net.cabezudo.sofia.web.client;

import java.math.BigDecimal;

public class Longitude {
  private final BigDecimal value;

  public Longitude(BigDecimal value) {
    this.value = value;
  }

  public BigDecimal getValue() {
    return value;
  }
}
