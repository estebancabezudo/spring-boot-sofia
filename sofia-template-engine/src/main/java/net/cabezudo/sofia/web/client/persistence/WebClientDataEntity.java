package net.cabezudo.sofia.web.client.persistence;

import net.cabezudo.sofia.accounts.persistence.AccountEntity;


import java.sql.Date;

public class WebClientDataEntity {
  private final Integer id;
  private final String language;
  private final AccountEntity account;
  private final Date lastUpdate;

  public WebClientDataEntity(Integer id, String language, AccountEntity accountEntity, Date last_update) {
    this.id = id;
    this.language = language;
    this.account = accountEntity;
    this.lastUpdate = last_update;
  }

  @Override
  public String toString() {
    return "WebClientDataEntity{" +
        "id=" + id +
        ", language='" + language + '\'' +
        ", account=" + account +
        ", lastUpdate=" + lastUpdate +
        '}';
  }

  public int getId() {
    return id;
  }

  public String getLanguage() {
    return language;
  }

  public AccountEntity getAccount() {
    return account;
  }

  public Date getLastUpdate() {
    return lastUpdate;
  }

}
