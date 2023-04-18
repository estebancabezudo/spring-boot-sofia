package net.cabezudo.sofia.web.client.mappers;

import net.cabezudo.sofia.countries.Country;
import net.cabezudo.sofia.web.client.City;
import net.cabezudo.sofia.web.client.CountryLanguages;
import net.cabezudo.sofia.web.client.Language;
import net.cabezudo.sofia.web.client.Latitude;
import net.cabezudo.sofia.web.client.Longitude;
import net.cabezudo.sofia.web.client.Region;
import net.cabezudo.sofia.web.client.rest.RestWebClient;
import net.cabezudo.sofia.web.client.service.WebClient;

import java.math.BigDecimal;

public class RestToBusinessWebClientMapper {
  public WebClient map(RestWebClient c) {
    if (c == null) {
      return null;
    }
    int id = c.getId();
    Country country = new Country(1, "mx");
    Region region = new Region("Quintana Roo");
    City city = new City("Cozumel");
    Latitude latitude = new Latitude(new BigDecimal("1.1"));
    Longitude longitude = new Longitude(new BigDecimal("1.1"));
    CountryLanguages languages = new CountryLanguages();
    Language language = new Language(c.getLanguage());
    return new WebClient(id, language);
  }
}