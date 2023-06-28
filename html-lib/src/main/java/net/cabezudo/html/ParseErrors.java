package net.cabezudo.html;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.01.00, 2022.10.05
 */
public class ParseErrors {

  private final List<ParseError> list = new ArrayList<>();

  public void add(ParseError error) {
    list.add(error);
  }

  public ParseError get(int i) {
    return list.get(i);
  }

  public boolean isEmpty() {
    return list.isEmpty();
  }

}
