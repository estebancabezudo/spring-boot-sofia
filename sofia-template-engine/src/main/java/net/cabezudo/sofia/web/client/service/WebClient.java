package net.cabezudo.sofia.web.client.service;

import net.cabezudo.sofia.web.client.Language;

public class WebClient {
  public static final String OBJECT_NAME_IN_SESSION = "webClientObject";

  private final int id;
  private final Language language;

  public WebClient(int id, Language language) {
    this.id = id;
    this.language = language;
  }

  public int getId() {
    return id;
  }

  public Language getLanguage() {
    return language;
  }

  @Override
  public String toString() {
    return "WebClient{" +
        "id=" + id +
        ", language=" + language +
        '}';
  }
}
