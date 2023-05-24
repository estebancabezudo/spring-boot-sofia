package net.cabezudo.sofia.sites;

import org.springframework.stereotype.Component;

@Component
public class EntityToBusinessSiteMapper {
  public Site map(SiteEntity e) {
    return new Site(e.getId(), e.getName());
  }
}
