package net.cabezudo.sofia.web.client.mappers;

import net.cabezudo.sofia.web.client.persistence.WebClientEntity;
import net.cabezudo.sofia.web.client.service.WebClient;

public class BusinessToEntityWebClientMapper {
  public WebClientEntity map(WebClient c) {
    int id = c.getId();
    String language = c.getLanguage().getValue();
    return new WebClientEntity(id, language);
  }
}
