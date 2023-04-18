package net.cabezudo.json.objects;

import net.cabezudo.json.annotations.JSONProperty;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.9, 08/02/2016
 */
public class Word {

  @JSONProperty(name = "version")
  private final Version VERSION = new Version();

  @JSONProperty
  private final long id = 2;

  @JSONProperty
  private final Language language = new Language();

  @JSONProperty
  private final String string = "افغانستان";

  public long getId() {
    return id;
  }

  public Language getLanguage() {
    return language;
  }

  public String getString() {
    return string;
  }

  public Version getVersion() {
    return VERSION;
  }
}
