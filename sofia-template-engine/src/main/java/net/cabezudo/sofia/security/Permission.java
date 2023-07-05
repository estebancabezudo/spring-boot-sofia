package net.cabezudo.sofia.security;

import net.cabezudo.html.InvalidParameterException;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Objects;

public class Permission implements Comparable<Permission> {
  public static final String ACCESS_GRANT = "grant";
  public static final String ACCESS_DENY = "deny";
  public static final String USER_ALL = "all";
  public static final String GROUP_ALL = "all";
  private final String data;
  private final String group;
  private final String user;
  private final String access;
  private final String resource;

  public Permission(String data) {
    String[] components = data.split(":");
    if (components.length < 4) {
      throw new InvalidParameterException("The permission string is invalid");
    }
    this.data = data;
    this.group = components[0];
    this.user = components[1];
    this.access = components[2];
    this.resource = components[3];
  }

  public Permission(String group, String user, String access, String resource) {
    this.data = group + ":" + user + ":" + access + ":" + resource;
    this.group = group;
    this.user = user;
    this.access = access;
    this.resource = resource;
  }

  public String getData() {
    return data;
  }

  public String getGroupName() {
    return group;
  }

  public String getUser() {
    return user;
  }

  public String getAccess() {
    return access;
  }

  public String getResource() {
    return resource;
  }

  @Override
  public int compareTo(@NotNull Permission permission) {
    return this.data.compareTo(permission.getData());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Permission that = (Permission) o;
    return data.equals(that.data);
  }

  @Override
  public int hashCode() {
    return Objects.hash(data);
  }

  public boolean containSome(Collection<? extends GrantedAuthority> authorities) {
    // TODO implement this
    return false;
  }

  @Override
  public String toString() {
    return data;
  }
}
