package net.cabezudo.sofia.users;

import net.cabezudo.sofia.sites.Site;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class SofiaUser implements UserDetails {
  private final int id;
  private final int siteId;
  private final String username;
  private final String password;
  private final Groups groups;
  private final boolean enabled;
  private Site site;


  public SofiaUser(int id, Site site, String username, String password, Groups groups, boolean enabled) {
    this.id = id;
    this.siteId = site.getId();
    this.site = site;
    this.username = username;
    this.password = password;
    this.groups = groups;
    this.enabled = enabled;
  }

  public SofiaUser(int id, Site site, String username, String password, Collection<GrantedAuthority> authorities, boolean enabled) {
    this.id = id;
    this.siteId = site.getId();
    this.site = site;
    this.username = username;
    this.password = password;
    this.groups = new Groups(authorities);
    this.enabled = enabled;
  }

  public SofiaUser(int id, int siteId, String username, String password, Groups groups, boolean enabled) {
    this.id = id;
    this.siteId = siteId;
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

  public Site getSite() {
    return site;
  }
}
