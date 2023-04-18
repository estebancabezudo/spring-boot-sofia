package net.cabezudo.html;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.01.00, 2022.10.05
 */
public class ParseError {

  private final String message;

  ParseError(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return message;
  }

}
