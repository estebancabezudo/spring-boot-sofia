package net.cabezudo.json.objects;

import net.cabezudo.json.annotations.JSONProperty;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.9, 08/02/2016
 */
public class Version {

  @JSONProperty
  private final int id = 1;

  @JSONProperty
  private final int minor = 0;

  @JSONProperty
  private final int version = 1;

  public int getId() {
    return id;
  }

  public int getMinor() {
    return minor;
  }

  public int getVersion() {
    return version;
  }
}
