package net.cabezudo.sofia.config.mail;

import net.cabezudo.sofia.core.templates.MailTemplate;


public class SofiaMailMessage {
  private final NamedEMail from;
  private final NamedEMail to;
  private final String subject;
  private final String body;

  private SofiaMailMessage(Builder builder) {
    this.from = builder.from;
    this.to = builder.to;
    this.subject = builder.subject;
    this.body = builder.body;
  }

  public NamedEMail getTo() {
    return to;
  }

  public NamedEMail getFrom() {
    return from;
  }

  public String getSubject() {
    return subject;
  }

  public String getBody() {
    return body;
  }

  public static class Builder {
    private NamedEMail from;
    private NamedEMail to;
    private String subject;
    private String body;

    public Builder from(NamedEMail from) {
      this.from = from;
      return this;
    }

    public Builder to(NamedEMail to) {
      this.to = to;
      return this;
    }

    public Builder load(MailTemplate mailTemplate) {
      subject = mailTemplate.getSubject();
      body = mailTemplate.getBody();
      return this;
    }

    public SofiaMailMessage build() {
      return new SofiaMailMessage(this);
    }

  }
}
