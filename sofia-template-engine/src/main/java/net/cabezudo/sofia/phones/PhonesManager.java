package net.cabezudo.sofia.phones;

import net.cabezudo.sofia.countries.CountryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Component
public class PhonesManager {

  private final List<Integer> phoneCountryCodes = new ArrayList<>();
  private final Map<Integer, String> countryCodeByPhoneCountryCode = new TreeMap<>();
  private final Map<String, Integer> phoneCountryCodeByCountryCode = new TreeMap<>();
  private @Autowired CountryManager countryManager;

  PhonesManager() {
    add("mx", 52);
  }

  private void add(String countryCode, Integer phoneCountryCode) {
    phoneCountryCodes.add(phoneCountryCode);
    phoneCountryCodeByCountryCode.put(countryCode, phoneCountryCode);
    countryCodeByPhoneCountryCode.put(phoneCountryCode, countryCode);
  }

  public Phone get(Long fullPhoneNumber) throws InvalidPhoneNumberException {
    if (fullPhoneNumber == 0) {
      return null;
    }
    return get(fullPhoneNumber.toString());
  }

  public Phone get(String fullStringPhoneNumber) throws InvalidPhoneNumberException {
    if (fullStringPhoneNumber.isBlank()) {
      return null;
    }
    for (Integer phoneCountryCode : phoneCountryCodes) {
      String stringPhoneCountryCode = phoneCountryCode.toString();
      if (fullStringPhoneNumber.startsWith(stringPhoneCountryCode)) {
        String phoneNumber = fullStringPhoneNumber.substring(stringPhoneCountryCode.length());
        return new Phone(phoneCountryCode, Long.parseLong(phoneNumber));
      }
    }
    throw new InvalidPhoneNumberException(fullStringPhoneNumber);
  }

  public Phone get(int countryCode, long number) {
    return new Phone(countryCode, number);
  }
}
