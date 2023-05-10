package net.cabezudo.sofia.accounts.persistence;

public class AccountEntity {
  private final int id;
  private final int siteId;

  public AccountEntity(int id, int siteId) {
    this.id = id;
    this.siteId = siteId;
  }

  public int getId() {
    return id;
  }

  public int getSiteId() {
    return siteId;
  }
}
