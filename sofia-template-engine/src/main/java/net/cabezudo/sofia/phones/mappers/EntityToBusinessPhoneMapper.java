package net.cabezudo.sofia.phones.mappers;

import net.cabezudo.sofia.phones.Phone;
import net.cabezudo.sofia.phones.persistence.PhoneEntity;
import org.springframework.stereotype.Component;

@Component
public class EntityToBusinessPhoneMapper {
  // Map the full object using the database entity
  public Phone map(PhoneEntity phoneEntity) {
    if (phoneEntity == null) {
      return null;
    }
    int countryCode = phoneEntity.getCountryCode();
    long number = phoneEntity.getNumber();
    return map(countryCode, number);
  }

  // Map the full object using all the separate parameters needed
  public Phone map(Integer countryCode, Long number) {
    if (countryCode == null && number == null) {
      return null;
    }
    return new Phone(countryCode, number);
  }
}
