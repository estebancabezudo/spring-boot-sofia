package net.cabezudo.sofia.users.persistence;

import net.cabezudo.sofia.emails.persistence.EMailEntity;

import javax.persistence.Column;

public class UserEntity {
  private final int id;
  @Column(name = "site_id")
  private final int siteId;
  private final EMailEntity eMailEntity;
  private final String password;
  private final GroupsEntity groupsEntity = new GroupsEntity();
  private final boolean enabled;
  private int accountUserId;

  public UserEntity(int id, int siteId, int accountUserId, EMailEntity eMailEntity, String password, boolean enabled) {
    this.id = id;
    this.siteId = siteId;
    this.accountUserId = accountUserId;
    this.eMailEntity = eMailEntity;
    this.password = password;
    this.enabled = enabled;
  }

  public int getAccountUserId() {
    return accountUserId;
  }

  public void setAccountUserId(int accountUserId) {
    this.accountUserId = accountUserId;
  }

  public int getId() {
    return id;
  }

  public int getSiteId() {
    return siteId;
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
