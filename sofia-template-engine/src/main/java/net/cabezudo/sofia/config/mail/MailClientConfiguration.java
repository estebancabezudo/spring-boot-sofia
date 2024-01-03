package net.cabezudo.sofia.config.mail;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
public class MailClientConfiguration {
  @Bean(name = "eMailClient")
  @Conditional(JavaMailSenderPropertiesExistsCondition.class)
  public EMailClient notificationSender(JavaMailSender javaMailSender) {
    return new JavaMailClient(javaMailSender);
  }

  @Bean(name = "eMailClient")
  @Conditional(MailJetPropertiesExistsCondition.class)
  public EMailClient jetMailClient() {
    return new JetMailClient();
  }
}
