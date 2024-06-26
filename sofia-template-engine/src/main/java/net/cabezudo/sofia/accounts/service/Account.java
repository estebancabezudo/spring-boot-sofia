package net.cabezudo.sofia.accounts.service;

import net.cabezudo.sofia.sites.Site;

import java.util.Objects;

public class Account implements Comparable<Account> {
  private final Integer id;
  private final Site site;
  private final String name;

  public Account(Integer id, Site site, String name) {
    this.id = id;
    this.site = site;
    this.name = name;
  }

  public Integer getId() {
    return id;
  }

  public Site getSite() {
    return site;
  }

  @Override
  public String toString() {
    return "Account[" +
        "id=" + id + ", " +
        "siteId=" + site + ", " +
        "name=" + name + ']';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Account account)) return false;
    return getId().equals(account.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }

  public String getName() {
    return name;
  }

  @Override
  public int compareTo(Account o) {
    return Integer.compare(this.id, o.id);
  }
}