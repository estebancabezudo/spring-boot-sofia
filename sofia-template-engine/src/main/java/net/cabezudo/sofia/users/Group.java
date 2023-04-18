package net.cabezudo.sofia.users;

public record Group(String name) implements Comparable<Group> {
  public static final String ADMIN = "admin";

  @Override
  public int compareTo(Group o) {
    return this.name.compareTo(o.name);
  }
}
