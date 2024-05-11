package net.cabezudo.sofia.sites;


import net.cabezudo.sofia.config.ConfigurationException;
import net.cabezudo.sofia.core.SofiaEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
  public static final String COMMONS_LIBRARIES_FILE_NAME = "libs.txt";
  private static final Logger log = LoggerFactory.getLogger(Site.class);
  private final String name;
  private final List<Locale> siteLocales = new ArrayList<>(); // TODO Load site languages from data created using site sources files
  private final int id;
  private final String replyAddress;
  private final Locale locale = new Locale("en"); // TODO  Change to use the language of the constructor, the one defined for the site in the configuration, or English.
  private final String loginSuccessURL;
  private Path sourcePath;

  public Site(int id, String name, String replyAddress, String loginSuccessURL) {
    this.id = id;
    this.name = name;
    this.replyAddress = replyAddress;
    this.loginSuccessURL = loginSuccessURL;
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
  public int compareTo(Site site) {
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

  public String getLoginSuccessURL() {
    return loginSuccessURL;
  }

  public void setSourcePath(String siteSourcesPathString, Path systemSourcePath) throws ConfigurationException {

    Path siteSourcePath = siteSourcesPathString == null ? null : Paths.get(siteSourcesPathString).resolve(name);
    if (siteSourcePath != null && Files.exists(siteSourcePath)) {
      this.sourcePath = siteSourcePath;
      log.debug("Using site sources path: " + this.sourcePath);
    } else {
      this.sourcePath = systemSourcePath.resolve(this.getName());
      log.debug("Using system sources path: " + this.sourcePath);
    }
    if (!Files.exists(this.sourcePath)) {
      throw new ConfigurationException("Path not found: " + this.sourcePath);
    }
    log.debug("Source path: " + this.sourcePath);

  }

  public Path getSourcesPath() {
    return sourcePath;
  }

  public Path getVersionedSourcesPath(String version) {
    return getSourcesPath().resolve(version);
  }

  public Path getVersionedSourcesLibraryPath(String version) {
    return getSourcesPath().resolve(version).resolve(SofiaEnvironment.LIBS_DIRECTORY_NAME);
  }
}
