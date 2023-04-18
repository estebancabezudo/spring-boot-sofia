package net.cabezudo.sofia.sites;

public class EntityToBusinessSiteMapper {
  public Site map(SiteEntity e) {
    return new Site(e.getId(), e.getName());
  }
}
