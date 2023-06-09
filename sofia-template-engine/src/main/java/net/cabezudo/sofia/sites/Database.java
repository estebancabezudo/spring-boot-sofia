package net.cabezudo.sofia.sites;

public class Database {
  private final String url;

  public Database(String url) {
    this.url = url;
  }

  public String getURL() {
    return url;
  }
}
