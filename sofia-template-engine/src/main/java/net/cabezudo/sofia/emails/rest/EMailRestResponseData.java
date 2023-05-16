package net.cabezudo.sofia.emails.rest;

public class EMailRestResponseData {
  private final String address;

  public EMailRestResponseData(String address) {
    this.address = address;
  }

  public String getAddress() {
    return address;
  }
}
