package net.cabezudo.sofia.creator;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.01.00, 2022.03.16
 */
public class SourceAlreadyAdded extends Exception {

  private final CodeSource source;

  SourceAlreadyAdded(String message, CodeSource source) {
    super(message);
    this.source = source;
  }

  public CodeSource getSource() {
    return source;
  }

}
