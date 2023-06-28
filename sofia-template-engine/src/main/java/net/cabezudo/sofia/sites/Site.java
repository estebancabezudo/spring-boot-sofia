package net.cabezudo.sofia.sites;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.01.00, 2022.09.02
 */
public class Site implements Comparable<Site> {

  public static final String COMMONS_CONFIGURATION_FILE_NAME = "commons.json";
  public static final String COMMONS_CSS_FILE_NAME = "commons.css";
  public static final String COMMONS_SCRIPT_FILE_NAME = "commons.js";
  public static final String TEXTS_FILE_NAME = "texts.json";
  private static final Logger log = LoggerFactory.getLogger(Site.class);
  private final String name;
  private final List<Locale> siteLocales = new ArrayList<>(); // TODO Load site languages from data created using site sources files
  private final int id;
  private final String replyAddress;
  private final Locale locale = new Locale("en"); // TODO  Change to use the language of the constructor, the one defined for the site in the configuration, or English.

  public Site(int id, String name, String replyAddress) {
    this.id = id;
    this.name = name;
    this.replyAddress = replyAddress;
  }

  public int getId() {
    return id;
  }

  public Locale getLocale() {
    return locale;
  }

  public String getReplyAddress() {
    return replyAddress;
  }

  public String getName() {
    return name;
  }

  @Override
  public int compareTo(@NotNull Site site) {
    return getName().compareTo(site.getName());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Site site = (Site) o;
    return name.equals(site.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }

  public Locale getDefaultLocale() {
    return locale;
  }

  public List<Locale> getSiteLocales() {
    return siteLocales;
  }

  public String toString() {
    return "[ name=" + getName() + ", locale=" + getDefaultLocale() + ", locales=" + getSiteLocales() + "]";
  }

}
