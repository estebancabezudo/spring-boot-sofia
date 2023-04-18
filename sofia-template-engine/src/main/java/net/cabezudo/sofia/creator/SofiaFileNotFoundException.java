package net.cabezudo.sofia.creator;

import net.cabezudo.html.nodes.FilePosition;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.01.00, 2022.03.15
 */
public class SofiaFileNotFoundException extends Exception {

  private final FilePosition position;

  SofiaFileNotFoundException(String message, FilePosition position) {
    super(message);
    this.position = position;
  }

  public FilePosition getPosition() {
    return position;
  }
}
