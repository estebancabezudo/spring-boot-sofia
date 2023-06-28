package net.cabezudo.sofia.users.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.cabezudo.sofia.accounts.Account;

public class RestUser {
  private final int id;
  private final String username;
  private final RestGroups groups;
  private final boolean enabled;
  private String locale;
  @JsonIgnore
  private Account account;
  @JsonIgnore
  private String password;

  public RestUser(int id, Account account, String username, String password, RestGroups groups, String locale, boolean enabled) {
    this.id = id;
    this.account = account;
    this.username = username;
    this.password = password;
    this.groups = groups;
    this.locale = locale;
    this.enabled = enabled;
  }

  public int getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public RestGroups getGroups() {
    return groups;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public Account getAccount() {
    return account;
  }

  public void setAccount(Account account) {
    this.account = account;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getLocale() {
    return locale;
  }

  public void setLocale(String locale) {
    this.locale = locale;
  }
}
