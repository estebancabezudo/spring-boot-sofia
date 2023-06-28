package net.cabezudo.sofia.web.client.mappers;

import net.cabezudo.sofia.accounts.Account;
import net.cabezudo.sofia.accounts.AccountManager;
import net.cabezudo.sofia.countries.Country;
import net.cabezudo.sofia.web.client.City;
import net.cabezudo.sofia.web.client.CountryLanguages;
import net.cabezudo.sofia.web.client.Language;
import net.cabezudo.sofia.web.client.Latitude;
import net.cabezudo.sofia.web.client.Longitude;
import net.cabezudo.sofia.web.client.Region;
import net.cabezudo.sofia.web.client.WebClientData;
import net.cabezudo.sofia.web.client.rest.RestWebClientData;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

public class RestToBusinessWebClientMapper {
  private @Autowired AccountManager accountManager;

  public WebClientData map(RestWebClientData c) {
    if (c == null) {
      return null;
    }
    Country country = new Country(1, "mx");
    Region region = new Region("Quintana Roo");
    City city = new City("Cozumel");
    Latitude latitude = new Latitude(new BigDecimal("1.1"));
    Longitude longitude = new Longitude(new BigDecimal("1.1"));
    CountryLanguages languages = new CountryLanguages();
    Language language = new Language(c.getLanguage());
    Account account = accountManager.get(c.getAccount().getId());
    return new WebClientData(language, account);
  }
}