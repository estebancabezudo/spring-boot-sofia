package users.persistence;

import javax.persistence.Column;

public class UserEntity {
  private final int id;
  @Column(name = "site_id")
  private final int siteId;
  private final String username;
  private final String password;
  private final GroupsEntity groupsEntity = new GroupsEntity();
  private final boolean enabled;

  public UserEntity(int id, int siteId, String username, String password, boolean enabled) {
    this.id = id;
    this.siteId = siteId;
    this.username = username;
    this.password = password;
    this.enabled = enabled;
  }

  public int getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public GroupsEntity getEntityGroups() {
    return this.groupsEntity;
  }

  public void add(GroupEntity group) {
    this.groupsEntity.add(group);
  }
}
