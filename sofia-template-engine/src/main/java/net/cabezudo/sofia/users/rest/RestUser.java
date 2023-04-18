package net.cabezudo.sofia.users.rest;

public class RestUser {
  private final int id;
  private final String username;
  private final RestGroups groups;
  private final boolean enabled;

  public RestUser(int id, String username, RestGroups groups, boolean enabled) {
    this.id = id;
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
}
