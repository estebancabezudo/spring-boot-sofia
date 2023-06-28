package net.cabezudo.sofia.config.mail;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class JavaMailClient implements EMailClient {
  private final JavaMailSender mailSender;

  public JavaMailClient(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  @Override
  public void send(SofiaMailMessage sofiaMailMessage) throws SendEMailException {
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    try {
      MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
      helper.setTo(sofiaMailMessage.getTo().getEMail().getAddress());
      helper.setSubject(sofiaMailMessage.getSubject());
      helper.setText(sofiaMailMessage.getBody());
    } catch (MessagingException e) {
      throw new RuntimeException(e);
    }
    mailSender.send(mimeMessage);
  }
}



