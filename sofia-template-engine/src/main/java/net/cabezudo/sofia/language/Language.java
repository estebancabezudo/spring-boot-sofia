package net.cabezudo.sofia.language;

import net.cabezudo.sofia.core.InvalidParameterException;

import java.util.Locale;
import java.util.Objects;

public final class Language {
  private final String code;

  public Language(String code) {
    if (code == null) {
      throw new InvalidParameterException("The code for language can't be null.");
    }
    this.code = code;
  }

  public String getCode() {
    return code;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (Language) obj;
    return Objects.equals(this.code, that.code);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code);
  }

  @Override
  public String toString() {
    return "Language{" +
        "value='" + code + '\'' +
        '}';
  }

  public Locale toLocale() {
    return new Locale(code);
  }
}
