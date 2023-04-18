package net.cabezudo.json.objects;

import net.cabezudo.json.annotations.JSONProperty;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.9, 08/02/2016
 */
public class CountryName {

  @JSONProperty(name = "version")
  private final Version VERSION = new Version();

  @JSONProperty
  private final Language language = new Language();

  @JSONProperty(field = "index")
  private final NameType nameType = NameType.LONG;

  @JSONProperty
  private final Word word = new Word();

  public Language getLanguage() {
    return language;
  }

  public NameType getNameType() {
    return nameType;
  }

  public Version getVersion() {
    return VERSION;
  }

  public Word getWord() {
    return word;
  }
}
