package net.cabezudo.sofia.web.client.rest;

import net.cabezudo.sofia.users.rest.RestUser;

public class RestWebClient {
  public static final String COOKIE_ID_NAME = "sofiaWebClientObjectId";
  int id;
  String language;
  RestUser user;

  public RestWebClient(int id, String language, RestUser user) {
    this.id = id;
    this.language = language;
    this.user = user;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public RestUser getUser() {
    return user;
  }

  public void setUser(RestUser user) {
    this.user = user;
  }

  @Override
  public String toString() {
    return "RestWebClient{" + "language='" + language + '\'' + ", user=" + user + '}';
  }
}
