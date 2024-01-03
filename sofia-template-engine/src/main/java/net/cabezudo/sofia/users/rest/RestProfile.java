package net.cabezudo.sofia.users.rest;

import net.cabezudo.sofia.accounts.rest.RestAccounts;


public class RestProfile {
  private String username;
  private String accountName;
  private String name;
  private String lastName;
  private RestAccounts accounts;

  private RestProfile() {
    // Protect the instance
  }

  public static RestProfile getBuilder() {
    return new RestProfile();
  }

  public String getUsername() {
    return username;
  }

  public RestProfile setUsername(String username) {
    this.username = username;
    return this;
  }

  public String getAccountName() {
    return accountName;
  }

  public RestProfile setAccountName(String accountName) {
    this.accountName = accountName;
    return this;
  }

  public String getName() {
    return name;
  }

  public RestProfile setName(String name) {
    this.name = name;
    return this;
  }

  public String getLastName() {
    return lastName;
  }

  public RestProfile setLastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  public RestAccounts getAccounts() {
    return accounts;
  }

  public RestProfile set(RestAccounts accounts) {
    this.accounts = accounts;
    return this;
  }

  public RestProfile build() {
    RestProfile restProfile = new RestProfile();
    restProfile.setUsername(username);
    restProfile.setAccountName(accountName);
    restProfile.setName(name);
    restProfile.setLastName(lastName);
    restProfile.set(accounts);
    return restProfile;
  }
}
