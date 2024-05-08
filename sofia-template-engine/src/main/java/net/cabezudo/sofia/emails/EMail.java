package net.cabezudo.sofia.emails;


import net.cabezudo.sofia.core.InvalidParameterException;

public class EMail {
  public static final int MAX_LENGTH = 200;

  private static String localPart;
  private final String address;
  private final boolean hasArroba;
  private final String domain;

  public EMail(String address) {
    if (address == null) {
      throw new InvalidParameterException("The address parameter cant be null");
    }

    this.address = address;
    int i = address.indexOf("@");
    if (i == -1) {
      localPart = address;
      this.hasArroba = false;
    } else {
      localPart = address.substring(0, i);
      this.hasArroba = true;
    }
    this.domain = address.substring(i + 1);
  }

  public static String getLocalPart() {
    return localPart;
  }

  public String getAddress() {
    return address;
  }

  public boolean hasArroba() {
    return hasArroba;
  }

  public String getDomain() {
    return domain;
  }
}
