package net.cabezudo.sofia.accounts.persistence;


import java.util.Objects;

public final class AccountUserRelationEntity {
  private final int id;
  private final int accountId;
  private final int userId;
  private final boolean owner;

  public AccountUserRelationEntity(int id, int accountId, int userId, boolean owner) {
    this.id = id;
    this.accountId = accountId;
    this.userId = userId;
    this.owner = owner;
  }

  public int getId() {
    return id;
  }

  public int getAccountId() {
    return accountId;
  }

  public int getUserId() {
    return userId;
  }

  public boolean getOwner() {
    return owner;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (AccountUserRelationEntity) obj;
    return this.id == that.id &&
        this.accountId == that.accountId &&
        this.userId == that.userId &&
        this.owner == that.owner;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, accountId, userId, owner);
  }

  @Override
  public String toString() {
    return "AccountUserRelationEntity[" +
        "id=" + id + ", " +
        "accountId=" + accountId + ", " +
        "userId=" + userId + ", " +
        "owner=" + owner + ']';
  }

}
