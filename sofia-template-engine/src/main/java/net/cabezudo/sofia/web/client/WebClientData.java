package net.cabezudo.sofia.web.client;

import net.cabezudo.sofia.accounts.Account;

import java.sql.Date;

public class WebClientData {
  private final Integer id;
  private final Date lastUpdate;
  private Language language;
  private Account account;
  private String message;

  public WebClientData(Integer id, Language language, Account account, Date lastUpdate) {
    this.id = id;
    this.language = language;
    this.account = account;
    this.lastUpdate = lastUpdate;
  }

  public Integer getId() {
    return id;
  }

  public Account getAccount() {
    return account;
  }

  public void setAccount(Account account) {
    this.account = account;
  }

  public Language getLanguage() {
    return language;
  }

  public void setLanguage(Language language) {
    this.language = language;
  }

  public void clearMessage() {
    this.message = null;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Date getLastUpdate() {
    return lastUpdate;
  }

  @Override
  public String toString() {
    return "WebClientData{" +
        "id=" + id +
        ", lastUpdate=" + lastUpdate +
        ", language=" + language +
        ", account=" + account +
        ", message='" + message + '\'' +
        '}';
  }
}
