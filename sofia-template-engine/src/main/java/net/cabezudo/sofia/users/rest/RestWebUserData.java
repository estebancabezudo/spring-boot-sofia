package net.cabezudo.sofia.users.rest;

import net.cabezudo.sofia.accounts.rest.RestAccount;
import net.cabezudo.sofia.web.client.rest.RestLanguage;

public class RestWebUserData {
  private final RestLanguage language;
  private final RestAccount account;
  private final RestUser user;

  public RestWebUserData(RestLanguage restLanguage, RestAccount restAccount, RestUser restUser) {
    this.language = restLanguage;
    this.account = restAccount;
    this.user = restUser;

  }

  public RestLanguage getLanguage() {
    return language;
  }

  public RestAccount getAccount() {
    return account;
  }

  public RestUser getUser() {
    return user;
  }
}
