package net.cabezudo.sofia.phones.mappers;

import net.cabezudo.sofia.phones.Phone;
import net.cabezudo.sofia.phones.PhoneList;
import net.cabezudo.sofia.phones.PhonesManager;
import net.cabezudo.sofia.phones.rest.RestPhone.RestPhone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RestToBusinessPhoneListMapper {
  private @Autowired PhonesManager phonesManager;

  public PhoneList map(List<RestPhone> phones) {
    PhoneList phoneList = new PhoneList();
    if (phones != null) {
      for (RestPhone restPhone : phones) {
        Phone phone = phonesManager.get(restPhone.getCountryCode(), restPhone.getNumber());
        phoneList.add(phone);
      }
    }
    return phoneList;
  }
}
