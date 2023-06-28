package net.cabezudo.sofia.web.client;

import net.cabezudo.sofia.accounts.Account;
import net.cabezudo.sofia.users.SofiaUser;

public class WebClientData {
  private final Language language;
  private Account account;
  private SofiaUser user;

  public WebClientData(Language language, Account account) {
    this.language = language;
    this.account = account;
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

  public SofiaUser getUser() {
    return user;
  }

  public void setUser(SofiaUser user) {
    this.user = user;
  }
}
