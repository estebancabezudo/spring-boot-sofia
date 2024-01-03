package net.cabezudo.sofia.config.mail;


import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class MailJetPropertiesExistsCondition implements Condition {
  @Override
  public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    Environment propertyResolver = context.getEnvironment();
    boolean publicKey = propertyResolver.containsProperty("spring.mail.mailJet.apiKey.public");
    boolean privateKey = propertyResolver.containsProperty("spring.mail.mailJet.apiKey.private");
    return publicKey && privateKey;
  }
}


