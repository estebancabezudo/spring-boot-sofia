package net.cabezudo.sofia.users.persistence;

import net.cabezudo.sofia.emails.persistence.EMailEntity;

import javax.persistence.Column;

public class UserEntity {
  private final int id;
  @Column(name = "site_id")
  private final int accountId;
  private final EMailEntity eMailEntity;
  private final String password;
  private final GroupsEntity groupsEntity = new GroupsEntity();
  private final boolean enabled;

  public UserEntity(int id, int accountId, EMailEntity eMailEntity, String password, boolean enabled) {
    this.id = id;
    this.accountId = accountId;
    this.eMailEntity = eMailEntity;
    this.password = password;
    this.enabled = enabled;
  }

  public int getId() {
    return id;
  }

  public int getAccountId() {
    return accountId;
  }

  public EMailEntity getEMailEntity() {
    return eMailEntity;
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
