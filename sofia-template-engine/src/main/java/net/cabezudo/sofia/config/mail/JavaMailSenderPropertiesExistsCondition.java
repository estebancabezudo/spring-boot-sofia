package net.cabezudo.sofia.config.mail;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class JavaMailSenderPropertiesExistsCondition implements Condition {
  @Override
  public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    Environment propertyResolver = context.getEnvironment();
    boolean host = propertyResolver.containsProperty("spring.mail.host");
    boolean port = propertyResolver.containsProperty("spring.mail.port");
    boolean username = propertyResolver.containsProperty("spring.mail.username");
    boolean password = propertyResolver.containsProperty("spring.mail.password");
    boolean auth = propertyResolver.containsProperty("spring.mail.properties.mail.smtp.auth");
    boolean enable = propertyResolver.containsProperty("spring.mail.properties.mail.smtp.starttls.enable");
    return host && port && username && password && auth && enable;
  }
}
