package net.cabezudo.sofia.countries;

import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CountryManager {

  private static final Logger log = LoggerFactory.getLogger(CountryManager.class);

  public Country get(int id) {
    // TODO Put this in database
    return new Country(1, "mx");
  }
}


