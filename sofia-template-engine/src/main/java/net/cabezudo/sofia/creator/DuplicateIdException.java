package net.cabezudo.sofia.creator;

import net.cabezudo.html.nodes.Element;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.01.00, 2022.04.09
 */
public class DuplicateIdException extends Exception {

  private final Element element;

  public DuplicateIdException(String message, Element element) {
    super(message);
    this.element = element;
  }

  public Element getElement() {
    return element;
  }

}
