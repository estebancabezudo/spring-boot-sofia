package net.cabezudo.sofia.web.client;

import java.math.BigDecimal;

public class Latitude {
  private final BigDecimal value;

  public Latitude(BigDecimal value) {
    this.value = value;
  }

  public BigDecimal getValue() {
    return value;
  }
}
