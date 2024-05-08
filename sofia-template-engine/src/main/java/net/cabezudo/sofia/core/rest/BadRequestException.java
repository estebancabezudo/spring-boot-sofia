package net.cabezudo.sofia.core.rest;

import net.cabezudo.sofia.accounts.service.InvalidAccountException;

public class BadRequestException extends Exception {
  public BadRequestException(String message, Throwable cause) {
    super(message, cause);
  }

  public BadRequestException(String message) {
    super(message);
  }

  public BadRequestException(InvalidAccountException cause) {
    super(cause);
  }
}
