package net.cabezudo.sofia.web.client;

import net.cabezudo.sofia.core.InvalidParameterException;

public record Language(String value) {
  public Language {
    if (value == null) {
      throw new InvalidParameterException("The code for language can't be null.");
    }
  }

  @Override
  public String toString() {
    return "Language{" +
        "value='" + value + '\'' +
        '}';
  }
}
