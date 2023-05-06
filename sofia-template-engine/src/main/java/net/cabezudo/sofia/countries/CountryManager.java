package net.cabezudo.sofia.countries;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class CountryManager {

  private static final Logger log = LoggerFactory.getLogger(CountryManager.class);

  public Country get(int id) {
    return new Country(1, "mx");
  }
}


