package net.cabezudo.sofia.emails.mappers;

import net.cabezudo.sofia.emails.EMail;
import net.cabezudo.sofia.emails.persistence.EMailEntity;
import org.springframework.stereotype.Component;

@Component
public class EntityToBusinessEMailMapper {
  public EMail map(EMailEntity eMailEntity) {
    if (eMailEntity == null) {
      return null;
    }
    return map(eMailEntity.getEmail());
  }

  // Map the object using the separate parameters from entity.
  public EMail map(String emailAddress) {
    if (emailAddress == null) {
      return null;
    }
    return new EMail(emailAddress);
  }
}
