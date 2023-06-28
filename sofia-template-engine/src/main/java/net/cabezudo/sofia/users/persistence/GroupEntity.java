package net.cabezudo.sofia.users.persistence;

import java.util.Objects;

public class GroupEntity {
  private final int accountUserId;
  private final String name;

  public GroupEntity(int accountUserId, String name) {
    this.accountUserId = accountUserId;
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof GroupEntity)) return false;
    GroupEntity that = (GroupEntity) o;
    return accountUserId == that.accountUserId && getName().equals(that.getName());
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountUserId, getName());
  }

  public String getName() {
    return name;
  }
}
