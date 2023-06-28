package net.cabezudo.sofia.config.mail;

public interface EMailClient {
  void send(SofiaMailMessage sofiaMailMessage) throws SendEMailException;
}
