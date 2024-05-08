package net.cabezudo.sofia.emails.mappers;

import net.cabezudo.sofia.emails.EMail;
import org.springframework.stereotype.Component;

@Component
public class RestToBusinessEMailMapper {
  public EMail map(String restEMail) {
    if (restEMail == null) {
      return null;
    }
    return new EMail(restEMail);
  }
}
