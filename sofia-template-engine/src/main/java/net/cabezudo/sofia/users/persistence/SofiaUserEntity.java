package net.cabezudo.sofia.users.persistence;

import net.cabezudo.sofia.accounts.persistence.AccountEntity;
import net.cabezudo.sofia.emails.persistence.EMailEntity;


public class SofiaUserEntity {
  private final int id;
  private final EMailEntity eMailEntity;
  private final String password;
  private final boolean enabled;
  private final String locale;
  private GroupsEntity groupsEntity = new GroupsEntity();
  private AccountEntity account;

  public SofiaUserEntity(int id, int accountId, int accountSiteId, String accountName, EMailEntity eMailEntity, String password, String locale, boolean enabled) {
    this.id = id;
    this.account = new AccountEntity(accountId, accountSiteId, accountName);
    this.eMailEntity = eMailEntity;
    this.password = password;
    this.locale = locale;
    this.enabled = enabled;
  }

  public SofiaUserEntity(int id, AccountEntity account, EMailEntity eMailEntity, String password, String locale, boolean enabled) {
    this.id = id;
    this.account = account;
    this.eMailEntity = eMailEntity;
    this.password = password;
    this.locale = locale;
    this.enabled = enabled;
  }

  public void setAccountUserId(AccountEntity account) {
    this.account = account;
  }

  public int getId() {
    return id;
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

  public GroupsEntity getGroups() {
    return this.groupsEntity;
  }

  public void setGroups(GroupsEntity groups) {
    this.groupsEntity = groups;
  }

  public void add(GroupEntity group) {
    this.groupsEntity.add(group);
  }

  public String getLocale() {
    return locale;
  }

  public AccountEntity getAccount() {
    return account;
  }

  public void setAccount(AccountEntity account) {
    this.account = account;
  }
}
