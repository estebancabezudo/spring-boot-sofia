package net.cabezudo.sofia.users.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.cabezudo.sofia.accounts.Account;

public class RestUser {
  private final int id;
  private final String username;
  private final RestGroups groups;
  private final boolean enabled;
  @JsonIgnore
  private Account account;

  public RestUser(int id, Account account, String username, RestGroups groups, boolean enabled) {
    this.id = id;
    this.account = account;
    this.username = username;
    this.groups = groups;
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
}
