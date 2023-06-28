package net.cabezudo.sofia.config.mail;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.transactional.SendContact;
import com.mailjet.client.transactional.SendEmailsRequest;
import com.mailjet.client.transactional.TrackOpens;
import com.mailjet.client.transactional.TransactionalEmail;
import com.mailjet.client.transactional.response.SendEmailsResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class JetMailClient implements EMailClient {

  @Value("${spring.mail.mailJet.apiKey.public}")
  private String publicKey;

  @Value("${spring.mail.mailJet.apiKey.private}")
  private String privateKey;

  private MailjetClient mailjetClient;

  @PostConstruct
  public void init() {
    ClientOptions options = ClientOptions.builder()
        .apiKey(publicKey)
        .apiSecretKey(privateKey)
        .build();
    mailjetClient = new MailjetClient(options);
  }

  @Override
  public void send(SofiaMailMessage sofiaMailMessage) throws SendEMailException {
    NamedEMail from = sofiaMailMessage.getFrom();
    NamedEMail to = sofiaMailMessage.getTo();
    TransactionalEmail transactionalEmail = TransactionalEmail
        .builder()
        .from(new SendContact(from.getEMail().getAddress(), from.getName()))
        .to(new SendContact(to.getEMail().getAddress(), to.getName()))
        .htmlPart(sofiaMailMessage.getBody())
        .subject(sofiaMailMessage.getSubject())
        .trackOpens(TrackOpens.ENABLED)
        .header("test-header-key", "test-value")
        .build();

    SendEmailsRequest request = SendEmailsRequest
        .builder()
        .message(transactionalEmail)
        .build();

    try {
      SendEmailsResponse response = request.sendWith(mailjetClient);
      System.out.println(response);
    } catch (MailjetException e) {
      throw new SendEMailException(e);
    }
  }
}



