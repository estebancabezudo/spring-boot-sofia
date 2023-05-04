package net.cabezudo.sofia.users.rest;

import net.cabezudo.sofia.sites.Site;

public class RestUser {
  private final int id;
  private final Site site;
  private final String username;
  private final RestGroups groups;
  private final boolean enabled;

  public RestUser(int id, Site site, String username, RestGroups groups, boolean enabled) {
    this.id = id;
    this.site = site;
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

    public Site getSite() {
    return site;
    }
}
