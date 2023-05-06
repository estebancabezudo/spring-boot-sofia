package net.cabezudo.sofia.users.persistence;

import java.util.Objects;

public class GroupEntity {
  private final int userId;
  private final String name;

  public GroupEntity(int userId, String name) {
    this.userId = userId;
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof GroupEntity that)) return false;
    return userId == that.userId && getName().equals(that.getName());
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, getName());
  }

  public String getName() {
    return name;
  }
}
