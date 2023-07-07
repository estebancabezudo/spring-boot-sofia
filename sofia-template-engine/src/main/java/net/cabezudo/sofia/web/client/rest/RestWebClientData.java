package net.cabezudo.sofia.web.client.rest;

import net.cabezudo.sofia.accounts.Account;
import net.cabezudo.sofia.users.rest.RestUser;

import java.util.Objects;

public final class RestWebClientData {
  private final String language;
  private final Account account;
  private final RestUser user;

  public RestWebClientData(String language, Account account, RestUser user) {
    this.language = language;
    this.account = account;
    this.user = user;
  }

  public String getLanguage() {
    return language;
  }

  public Account getAccount() {
    return account;
  }


  public RestUser getUser() {
    return user;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (RestWebClientData) obj;
    return Objects.equals(this.language, that.language) &&
        Objects.equals(this.account, that.account);
  }

  @Override
  public int hashCode() {
    return Objects.hash(language, account);
  }

  @Override
  public String toString() {
    return "RestWebClientData[" +
        "language=" + language + ", " +
        "account=" + account + ", " +
        "user=" + user + ']';
  }

}
