package net.cabezudo.json.objects;

import net.cabezudo.json.annotations.JSONProperty;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.9, 08/02/2016
 */
public class Language {

  @JSONProperty(name = "version")
  private final Version VERSION = new Version();
  @JSONProperty
  private final String charset = "utf8";
  @JSONProperty
  private final String collation = "utf8_general_ci";

  @JSONProperty
  private final int id = 97;

  @JSONProperty
  private final String letterCode = "ps";

  public String getCharset() {
    return charset;
  }

  public String getCollation() {
    return collation;
  }

  public int getId() {
    return id;
  }

  public String getLetterCode() {
    return letterCode;
  }

  public Version getVersion() {
    return VERSION;
  }

}
