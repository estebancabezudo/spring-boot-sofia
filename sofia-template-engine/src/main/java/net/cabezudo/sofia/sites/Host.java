package net.cabezudo.sofia.sites;

import net.cabezudo.sofia.core.InvalidParameterException;


public class Host {
  private final String name;
  private final String version;

  public Host(String name, String version) {
    this.name = name;
    this.version = version;

  }

  public Host(String hostname) {
    // TODO validate hostname format y values
    int i = hostname.indexOf(":");
    if (i == 0) {
      throw new InvalidParameterException("Invalid hostname format: ", hostname);
    }
    if (i > 0) {
      this.name = hostname.substring(0, i);
      this.version = hostname.substring(i + 1);
    } else {
      this.name = hostname;
      this.version = "1";
    }
  }

  public String getVersion() {
    return version;
  }

  public String getName() {
    return name;
  }
}
