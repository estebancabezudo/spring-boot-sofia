package net.cabezudo.sofia.users;

import net.cabezudo.sofia.accounts.Account;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Locale;

public class SofiaUser implements UserDetails {
  private final int id;
  private final String username;
  private final String password;
  private final Groups groups;
  private final boolean enabled;
  private final Locale locale;
  private final Account account;
  private final int accountId;

  public SofiaUser(int id, @NotNull Account account, @NotNull String username, String password, @NotNull Groups groups, Locale locale, boolean enabled) {
    this.id = id;
    this.accountId = account.getId();
    this.account = account;
    this.username = username;
    this.password = password;
    this.groups = groups;
    this.locale = locale;
    this.enabled = enabled;
  }


  public SofiaUser(int id, @NotNull Account account, @NotNull String username, String password, @NotNull Collection<GrantedAuthority> authorities, @NotNull Locale locale, boolean enabled) {
    this.id = id;
    this.accountId = account.getId();
    this.account = account;
    this.username = username;
    this.password = password;
    this.groups = new Groups(authorities);
    this.locale = locale;
    this.enabled = enabled;
  }

  @Override
  public String toString() {
    return "SofiaUser{" +
        "id=" + id +
        ", username='" + username + '\'' +
        ", groups=" + groups +
        ", locale=" + locale +
        ", enabled=" + enabled +
        ", accountId=" + accountId +
        ", account=" + account +
        '}';
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

  public Locale getLocale() {
    return locale;
  }
}
