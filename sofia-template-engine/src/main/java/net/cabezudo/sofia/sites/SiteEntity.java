package net.cabezudo.sofia.sites;

import java.util.Objects;

public final class SiteEntity {
  private final int id;
  private final String name;

  public SiteEntity(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public int id() {
    return id;
  }

  public String name() {
    return name;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (SiteEntity) obj;
    return this.id == that.id &&
        Objects.equals(this.name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }

  @Override
  public String toString() {
    return "SiteEntity[" +
        "id=" + id + ", " +
        "name=" + name + ']';
  }

}
