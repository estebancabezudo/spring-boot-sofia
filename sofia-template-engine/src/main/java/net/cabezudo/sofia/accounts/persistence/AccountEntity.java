package net.cabezudo.sofia.accounts.persistence;

import java.util.Objects;

public final class AccountEntity {
  private final int id;
  private final int siteId;
  private final String name;

  public AccountEntity(int id, int siteId, String name) {
    this.id = id;
    this.siteId = siteId;
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public int getSiteId() {
    return siteId;
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (AccountEntity) obj;
    return this.id == that.id &&
        this.siteId == that.siteId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, siteId);
  }

  @Override
  public String toString() {
    return "AccountEntity[" +
        "id=" + id + ", " +
        "siteId=" + siteId + ']';
  }

}
