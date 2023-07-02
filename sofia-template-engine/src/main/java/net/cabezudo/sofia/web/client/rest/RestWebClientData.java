package net.cabezudo.sofia.web.client.rest;

import net.cabezudo.sofia.accounts.Account;
import net.cabezudo.sofia.users.rest.RestUser;

import java.util.Objects;

public final class RestWebClientData {
  private final String language;
  private final Account account;
  private final RestUser user;
  private final String message;

  public RestWebClientData(String language, Account account, RestUser user, String message) {
    this.language = language;
    this.account = account;
    this.user = user;
    this.message = message;
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

  public String getMessage() {
    return message;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (RestWebClientData) obj;
    return Objects.equals(this.language, that.language) &&
        Objects.equals(this.account, that.account) &&
        Objects.equals(this.user, that.user);
  }

  @Override
  public int hashCode() {
    return Objects.hash(language, account, user);
  }

  @Override
  public String toString() {
    return "RestWebClientData[" +
        "language=" + language + ", " +
        "account=" + account + ", " +
        "user=" + user + ", " +
        "message=" + message + ']';
  }

}
