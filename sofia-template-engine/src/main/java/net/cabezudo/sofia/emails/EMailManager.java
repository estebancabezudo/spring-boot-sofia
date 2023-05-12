package net.cabezudo.sofia.emails;

import net.cabezudo.sofia.core.hostname.HostnameManager;
import net.cabezudo.sofia.hostnames.DomainNameValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EMailManager {
  private static final Logger log = LoggerFactory.getLogger(EMailManager.class);

  private @Autowired HostnameManager hostnameManager;

  public void info(String address) throws EMailAddressValidationException {
    if (address.isEmpty()) {
      throw new EMailAddressValidationException("emptyEMail");
    }
    if (address.length() > EMail.MAX_LENGTH) {
      throw new EMailAddressValidationException("eMailTooLong");
    }

    EMail eMail = new EMail(address);
    String localPart = EMail.getLocalPart();

    for (int j = 0; j < localPart.length(); j++) {
      char c = localPart.charAt(j);
      if (!Character.isLetterOrDigit(c) && c != '.' && c != '_') {
        throw new EMailAddressValidationException("invalidEMailAddress");
      }
    }

    if (!eMail.hasArroba()) {
      throw new EMailAddressValidationException("arrobaMissing");
    }

    String domainName = eMail.getDomain();
    try {
      hostnameManager.info(domainName);
    } catch (DomainNameValidationException e) {
      throw new EMailAddressValidationException(e.getMessage());
    }
  }
}
