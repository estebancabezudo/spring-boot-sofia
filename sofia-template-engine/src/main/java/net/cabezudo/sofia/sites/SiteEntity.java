package net.cabezudo.sofia.sites;

public class SiteEntity {
  private final int id;
  private final String name;

  public SiteEntity(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}
