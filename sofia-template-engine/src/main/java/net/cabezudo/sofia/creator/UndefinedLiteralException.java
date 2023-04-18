package net.cabezudo.sofia.creator;

import net.cabezudo.html.nodes.Position;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.01.00, 2019.06.06
 */
public class UndefinedLiteralException extends SiteCreationException {

  private final String literal;

  UndefinedLiteralException(String literal, Position position, Throwable cause) {
    super("Undefined literal: " + literal + " in " + position, position, cause);
    this.literal = literal;
  }

  String getUndefinedLiteral() {
    return literal;
  }
}
