package net.cabezudo.sofia.web.client.mappers;

import net.cabezudo.sofia.web.client.rest.RestWebClient;
import net.cabezudo.sofia.web.client.service.WebClient;

public class BusinessToRestWebClientMapper {
  public RestWebClient map(WebClient c) {
    String language = c == null || c.getLanguage() == null ? null : c.getLanguage().getValue();
    return new RestWebClient(c.getId(), language, null);
  }
}
