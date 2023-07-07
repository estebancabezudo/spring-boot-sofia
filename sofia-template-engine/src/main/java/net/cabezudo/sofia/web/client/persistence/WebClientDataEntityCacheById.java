package net.cabezudo.sofia.web.client.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.TreeMap;

@Component
public class WebClientDataEntityCacheById { // Extends from a class to encapsulate the object with a max time from the configuration to share cache functionality
  private static final Logger log = LoggerFactory.getLogger(WebClientDataEntityCacheById.class);
  private final Map<Integer, WebClientDataEntity> map = new TreeMap<>();

  public WebClientDataEntity get(Integer id) {
    log.debug("Get from cache using " + id);
    if (id == null) {
      return null;
    }
    return map.get(id);
  }

  public void put(Integer id, WebClientDataEntity webClientDataEntity) {
    log.debug("Put " + webClientDataEntity + " in cache using " + id);
    map.put(id, webClientDataEntity);
    if (map.size() > 100) { // Take the max size from configuration
      // TODO clean the map from older data
      // TODO if size is the same after, send an alert to the administrator
    }
  }
}
