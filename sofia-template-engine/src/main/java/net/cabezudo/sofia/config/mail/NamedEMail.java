package net.cabezudo.sofia.config.mail;

import net.cabezudo.sofia.emails.EMail;

public class NamedEMail {
  private final String name;
  private final EMail email;

  public NamedEMail(String name, EMail eMail) {
    this.name = name;
    this.email = eMail;
  }

  public EMail getEMail() {
    return email;
  }

  public String getName() {
    return name;
  }
}
