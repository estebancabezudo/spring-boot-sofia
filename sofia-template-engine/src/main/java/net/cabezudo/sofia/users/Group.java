package net.cabezudo.sofia.users;

import java.util.Objects;

public final class Group implements Comparable<Group> {
  public static final String ADMIN = "admin";
  public static final String USER = "user";
  private final String name;

  public Group(String name) {
    this.name = name;
  }

  @Override
  public int compareTo(Group o) {
    return this.name.compareTo(o.name);
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (Group) obj;
    return Objects.equals(this.name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }

  @Override
  public String toString() {
    return "Group[" +
        "name=" + name + ']';
  }

}
