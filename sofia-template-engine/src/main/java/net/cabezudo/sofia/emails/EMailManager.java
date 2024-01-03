package net.cabezudo.sofia.emails;

import net.cabezudo.sofia.config.mail.EMailClient;
import net.cabezudo.sofia.config.mail.NamedEMail;
import net.cabezudo.sofia.config.mail.SendEMailException;
import net.cabezudo.sofia.config.mail.SofiaMailMessage;
import net.cabezudo.sofia.core.SofiaEnvironment;
import net.cabezudo.sofia.core.hostname.HostnameManager;
import net.cabezudo.sofia.core.templates.MailTemplate;
import net.cabezudo.sofia.hostnames.DomainNameValidationException;
import net.cabezudo.sofia.sites.Site;
import net.cabezudo.sofia.users.service.Password;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EMailManager {
  private static final Logger log = LoggerFactory.getLogger(EMailManager.class);

  private @Autowired HostnameManager hostnameManager;

  @Autowired
  @Qualifier("eMailClient")
  private EMailClient eMailClient;
  private @Autowired SofiaEnvironment sofiaEnvironment;

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

  public String getPasswordUpdatedTemplateFileName() {
    // TODO Put in the configuration the name of the password updated template file
    return "passwordUpdatedTemplate.html";
  }

  public void sendPasswordUpdated(String fromName, EMail eMailFrom, String userName, EMail eMail, Locale locale, Password password, Site site) throws SendEMailException {
    File passwordUpdatedTemplateFileName = sofiaEnvironment.getSystemTemplatesPath().resolve("passwordUpdated-" + locale + ".txt").toFile();
    MailTemplate mailTemplate;
    try {
      mailTemplate = new MailTemplate.Builder()
          .set("password", password.toString())
          .set("siteName", site.getName())
          .set("userName", userName == null ? eMail.getAddress() : userName)
          .set(passwordUpdatedTemplateFileName)
          .build();
    } catch (IOException e) {
      throw new SendEMailException(e);
    }
    send(fromName, eMailFrom, userName, eMail, mailTemplate);
  }

  public void send(String fromName, EMail eMailFrom, String name, EMail eMailTo, MailTemplate mailTemplate) throws SendEMailException {
    NamedEMail from = new NamedEMail(fromName, eMailFrom);
    NamedEMail to = new NamedEMail(name, eMailTo);
    SofiaMailMessage sofiaMailMessage = new SofiaMailMessage.Builder().load(mailTemplate).from(from).to(to).build();
    eMailClient.send(sofiaMailMessage);
  }
}
