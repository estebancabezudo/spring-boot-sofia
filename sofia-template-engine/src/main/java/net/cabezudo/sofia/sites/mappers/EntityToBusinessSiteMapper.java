package net.cabezudo.sofia.sites.mappers;

import net.cabezudo.sofia.core.SofiaRuntimeException;
import net.cabezudo.sofia.sites.Site;
import net.cabezudo.sofia.sites.SiteEntity;
import net.cabezudo.sofia.sites.SiteNotFoundException;
import net.cabezudo.sofia.sites.service.SiteManager;
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
