package users.persistence;

import java.util.Objects;

public class GroupEntity {
  private final String name;

  public GroupEntity(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    GroupEntity that = (GroupEntity) o;
    return Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }

  public String getName() {
    return name;
  }
}
