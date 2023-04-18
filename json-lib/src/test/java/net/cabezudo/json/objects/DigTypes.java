package net.cabezudo.json.objects;

import net.cabezudo.json.annotations.JSONProperty;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.9, 08/05/2016
 */
public class DigTypes {

  @JSONProperty
  private final Types types = new Types();

  public Types getTypes() {
    return types;
  }
}
