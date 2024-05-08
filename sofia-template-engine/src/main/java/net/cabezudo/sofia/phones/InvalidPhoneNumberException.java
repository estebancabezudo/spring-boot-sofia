package net.cabezudo.sofia.phones;

import lombok.Getter;
import net.cabezudo.sofia.core.exceptions.InvalidDataException;

public class InvalidPhoneNumberException extends InvalidDataException {
  @Getter
  private final String phoneNumber;

  public InvalidPhoneNumberException(String phoneNumber) {
    super("Invalid number: " + phoneNumber);
    this.phoneNumber = phoneNumber;
  }
}
