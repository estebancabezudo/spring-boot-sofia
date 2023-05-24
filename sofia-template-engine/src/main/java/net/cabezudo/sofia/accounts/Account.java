package net.cabezudo.sofia.accounts;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record Account(Integer id, int siteId) implements Comparable<Account> {
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Account account = (Account) o;
    return id == account.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public int compareTo(@NotNull Account a) {
    return id.compareTo(a.id);
  }

  @Override
  public String toString() {
    return "Account{" +
        "id=" + id +
        ", siteId=" + siteId +
        '}';
  }
}
