package net.cabezudo.sofia.users.rest;

import net.cabezudo.sofia.accounts.rest.RestAccount;
import net.cabezudo.sofia.web.client.rest.RestLanguage;

public class RestWebUserData {
  private final RestLanguage language;
  private final RestAccount account;
  private final RestSofiaUser user;

  public RestWebUserData(RestLanguage restLanguage, RestAccount restAccount, RestSofiaUser restSofiaUser) {
    this.language = restLanguage;
    this.account = restAccount;
    this.user = restSofiaUser;

  }

  public RestLanguage getLanguage() {
    return language;
  }

  public RestAccount getAccount() {
    return account;
  }

  public RestSofiaUser getUser() {
    return user;
  }
}
