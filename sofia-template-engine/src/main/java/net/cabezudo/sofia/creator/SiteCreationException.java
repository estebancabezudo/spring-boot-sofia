package net.cabezudo.sofia.creator;

import net.cabezudo.html.nodes.Position;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.01.00, 2019.06.06
 */
public class SiteCreationException extends Exception {

  private final Position position;

  public SiteCreationException(String message) {
    super(message);
    this.position = null;
  }

  public SiteCreationException(String message, Throwable cause) {
    super(message, cause);
    this.position = null;
  }

  public SiteCreationException(String message, Position position, Throwable cause) {
    super(message, cause);
    this.position = position;
  }

  public SiteCreationException(Throwable cause) {
    super(cause);
    this.position = null;
  }

  public SiteCreationException(String message, Position position) {
    super(message);
    this.position = position;
  }

  public Position getPosition() {
    return position;
  }

}
