package net.cabezudo.sofia.users;

import net.cabezudo.sofia.accounts.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class SofiaUser implements UserDetails {
  private final int id;
  private final String username;
  private final String password;
  private final Groups groups;
  private final boolean enabled;
  private int accountId;
  private Account account;


  public SofiaUser(int id, Account account, String username, String password, Groups groups, boolean enabled) {
    this.id = id;
    this.accountId = account.id();
    this.account = account;
    this.username = username;
    this.password = password;
    this.groups = groups;
    this.enabled = enabled;
  }

  public SofiaUser(int id, Account account, String username, String password, Collection<GrantedAuthority> authorities, boolean enabled) {
    this.id = id;
    this.accountId = account.id();
    this.account = account;
    this.username = username;
    this.password = password;
    this.groups = new Groups(authorities);
    this.enabled = enabled;
  }

  public SofiaUser(int id, int accountId, String username, String password, Groups groups, boolean enabled) {
    this.id = id;
    this.accountId = accountId;
    this.username = username;
    this.password = password;
    this.groups = groups;
    this.enabled = enabled;
  }

  @Override
  public Collection<GrantedAuthority> getAuthorities() {
    return groups.toAuthorities();
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  public boolean hasPermission(String groupName) {
    return groups.contains(groupName);
  }

  public int getId() {
    return id;
  }

  public Groups getGroups() {
    return groups;
  }

  public Account getAccount() {
    return account;
  }

  public void setAccount(Account account) {
    this.account = account;
    this.accountId = account.id();
  }
}
