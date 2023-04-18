package net.cabezudo.json.objects;

import net.cabezudo.json.annotations.JSONProperty;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.9, 08/02/2016
 */
public class Data {

  @JSONProperty(name = "version")
  private final Version VERSION = new Version();

  @JSONProperty
  private final int countryId = 1;

  @JSONProperty
  private final CountryName countryName = new CountryName();

  public int getCountryId() {
    return countryId;
  }

  public CountryName getCountryName() {
    return countryName;
  }

  public Version getVersion() {
    return VERSION;
  }
}
