package net.cabezudo.sofia.sites;

import net.cabezudo.sofia.core.SofiaRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EntityToBusinessSiteMapper {
  private @Autowired SiteManager siteManager;

  public Site map(SiteEntity siteEntity) {
    Site site;
    try {
      site = siteManager.get(siteEntity.name());
    } catch (SiteNotFoundException e) {
      throw new SofiaRuntimeException(e);
    }
    return site;
  }
}
