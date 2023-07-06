package net.cabezudo.sofia.web.user;

import net.cabezudo.sofia.accounts.Account;
import net.cabezudo.sofia.users.service.SofiaUser;
import net.cabezudo.sofia.web.client.Language;

public class WebUserData {
  private Language language;
  private Account account;
  private SofiaUser user;

  public WebUserData(Language language, Account account, SofiaUser user) {
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

}
