package net.cabezudo.sofia.web.client.persistence;

public class WebClientEntity {
  public static final String ID_COLUMN_NAME = "id";
  public static final String LANGUAGE_COLUMN_NAME = "language";
  private final Integer id;
  private final String language;

  public WebClientEntity(Integer id, String language) {
    this.id = id;
    this.language = language;
  }

  public Integer getId() {
    return id;
  }

  public String getLanguage() {
    return language;
  }
}
