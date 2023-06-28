package net.cabezudo.sofia.hostnames.rest;

import java.util.Objects;

public final class HostnameRestResponse {
  private final String messageKey;

  HostnameRestResponse(String messageKey) {
    this.messageKey = messageKey;
  }

  public String messageKey() {
    return messageKey;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (HostnameRestResponse) obj;
    return Objects.equals(this.messageKey, that.messageKey);
  }

  @Override
  public int hashCode() {
    return Objects.hash(messageKey);
  }

  @Override
  public String toString() {
    return "HostnameRestResponse[" +
        "messageKey=" + messageKey + ']';
  }

}
