package net.cabezudo.sofia.core.hostname;

import net.cabezudo.sofia.hostnames.DomainNameValidationException;

import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Service
public class HostnameManager {

  private static final int NAME_MAX_LENGTH = 200;

  public void info(String name) throws DomainNameValidationException {
    if (name.isEmpty()) {
      throw new DomainNameValidationException("emptyHostName");
    }
    if (name.length() > NAME_MAX_LENGTH) {
      throw new DomainNameValidationException("hostnameTooLong");
    }

    for (int i = 0; i < name.length(); i++) {
      Character c = name.charAt(i);
      if (!Character.isLetterOrDigit(c) && c != '.' && c != '-' && c != '_') {
        throw new DomainNameValidationException("invalidCharacterInHostname");
      }
    }

    try {
      InetAddress.getByName(name);
    } catch (UnknownHostException e) {
      throw new DomainNameValidationException("hostnameNotExists");
    }
  }
}
