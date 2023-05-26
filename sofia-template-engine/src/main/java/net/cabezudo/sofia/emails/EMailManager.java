package net.cabezudo.sofia.emails;

import net.cabezudo.sofia.core.hostname.HostnameManager;
import net.cabezudo.sofia.hostnames.DomainNameValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
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

    if (localPart.length() == 0) {
      throw new EMailAddressValidationException("invalidEMailAddress");
    }

    if (!eMail.hasArroba()) {
      throw new EMailAddressValidationException("arrobaMissing");
    }

    String regex = "^[\\w!#$%&amp;'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&amp;'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    Pattern pattern = Pattern.compile(regex);

    Matcher matcher = pattern.matcher(address);
    if (!matcher.matches()) {
      throw new EMailAddressValidationException("invalidEMailAddress");
    }

    String domainName = eMail.getDomain();
    try {
      hostnameManager.info(domainName);
    } catch (DomainNameValidationException e) {
      throw new EMailAddressValidationException(e.getMessage());
    }
  }
}
