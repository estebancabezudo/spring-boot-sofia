package net.cabezudo.sofia.phones.mappers;

import net.cabezudo.sofia.phones.Phone;
import net.cabezudo.sofia.phones.PhoneList;
import net.cabezudo.sofia.phones.persistence.PhoneEntity;
import net.cabezudo.sofia.phones.persistence.PhoneEntityList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EntityToBusinessPhoneListMapper {
  private @Autowired EntityToBusinessPhoneMapper entityToBusinessPhoneMapper;

  public PhoneList map(PhoneEntityList phoneEntityList) {
    PhoneList phoneList = new PhoneList();
    if (phoneEntityList != null) {
      for (PhoneEntity phoneEntity : phoneEntityList) {
        Phone phone = entityToBusinessPhoneMapper.map(phoneEntity);
        phoneList.add(phone);
      }
    }
    return phoneList;
  }
}
