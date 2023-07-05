package net.cabezudo.sofia.web.client;

import net.cabezudo.sofia.accounts.Account;
import net.cabezudo.sofia.users.service.SofiaUser;

public class WebClientData {
  private Language language;
  private Account account;
  private SofiaUser user;
  private String message;

  public WebClientData(Language language, Account account, SofiaUser user) {
    this.language = language;
    this.account = account;
    this.user = user;
  }

  public Account getAccount() {
    return account;
  }

  public void setAccount(Account account) {
    this.account = account;
  }

  @Override
  public String toString() {
    return "WebClientData{" +
        "language=" + language +
        ", account=" + account +
        ", user=" + user +
        '}';
  }

  public Language getLanguage() {
    return language;
  }

  public void setLanguage(Language language) {
    this.language = language;
  }

  public SofiaUser getUser() {
    return user;
  }

  public void setUser(SofiaUser user) {
    this.user = user;
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
}
