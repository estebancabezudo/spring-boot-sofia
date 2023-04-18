package net.cabezudo.sofia.web.client;

import java.util.ArrayList;
import java.util.List;

public class CountryLanguages {
  private final List<Language> languages;

  public CountryLanguages(List languages) {
    this.languages = languages;
  }

  public CountryLanguages() {
    languages = new ArrayList();
  }

  public List<Language> getLanguages() {
    return languages;
  }
}
