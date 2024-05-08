package net.cabezudo.sofia.phones.mappers;

import net.cabezudo.sofia.phones.InvalidPhoneNumberException;
import net.cabezudo.sofia.phones.Phone;
import net.cabezudo.sofia.phones.PhonesManager;
import net.cabezudo.sofia.phones.rest.RestPhone.RestPhone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RestToBusinessPhoneMapper {
  private @Autowired PhonesManager phoneManager;

  public Phone map(RestPhone restPhone) throws InvalidPhoneNumberException {
    if (restPhone == null) {
      return null;
    }
    return phoneManager.get(restPhone.getCountryCode(), restPhone.getNumber());
  }
}
