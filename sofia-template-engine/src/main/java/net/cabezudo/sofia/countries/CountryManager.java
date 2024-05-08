package net.cabezudo.sofia.countries;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.TreeMap;

@Service
@Transactional
public class CountryManager {

  private static final Logger log = LoggerFactory.getLogger(CountryManager.class);

  private final Map<Integer, Country> countryById = new TreeMap<>();
  private final Map<String, Country> countryByCode = new TreeMap<>();

  CountryManager() {
    add(new Country(1, "mx", 52, "## #### ####"));
  }

  private void add(Country country) {
    countryById.put(country.getId(), country);
    countryByCode.put(country.getCode(), country);
  }

  public Country get(Integer id) {
    if (id == null) {
      return null;
    }
    return countryById.get(id);
  }

  public Country get(String countryCode) {
    if (countryCode == null) {
      return null;
    }
    return countryByCode.get(countryCode);
  }
}


