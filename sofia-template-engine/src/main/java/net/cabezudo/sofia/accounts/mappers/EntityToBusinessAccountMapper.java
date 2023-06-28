package net.cabezudo.sofia.accounts.mappers;

import net.cabezudo.sofia.accounts.Account;
import net.cabezudo.sofia.accounts.persistence.AccountEntity;
import net.cabezudo.sofia.sites.Site;
import net.cabezudo.sofia.sites.SiteManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EntityToBusinessAccountMapper {
  private @Autowired SiteManager siteManager;

  public Account map(AccountEntity entity) {
    Site site = siteManager.get(entity.getSiteId());
    return new Account(entity.getId(), site, entity.getName());
  }
}
