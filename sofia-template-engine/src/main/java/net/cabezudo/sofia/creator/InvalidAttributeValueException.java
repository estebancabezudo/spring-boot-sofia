package net.cabezudo.sofia.creator;

import net.cabezudo.html.nodes.FilePosition;

public class InvalidAttributeValueException extends SiteCreationException {
  public InvalidAttributeValueException(String message, FilePosition position) {
    super(message, position);
  }
}
