package net.cabezudo.sofia.web.client;

import net.cabezudo.sofia.core.InvalidParameterException;

public class Language {
  private final String value;

  public Language(String value) {
    if (value == null) {
      throw new InvalidParameterException("The code for language can't be null.");
    }
    this.value = value;
  }

  @Override
  public String toString() {
    return "Language{" +
        "value='" + value + '\'' +
        '}';
  }

  public String getValue() {
    return value;
  }
}
