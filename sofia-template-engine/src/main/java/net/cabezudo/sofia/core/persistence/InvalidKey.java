package net.cabezudo.sofia.core.persistence;

import org.springframework.dao.DataAccessException;

public class InvalidKey extends DataAccessException {
  public InvalidKey(String message) {
    super(message);
  }
}
