package net.cabezudo.sofia.sites.persistence;

import net.cabezudo.sofia.persistence.DatabaseManager;
import net.cabezudo.sofia.sites.SiteEntity;
import net.cabezudo.sofia.sites.mappers.SiteRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class SiteRepository {
  private static final Logger log = LoggerFactory.getLogger(SiteRepository.class);
  private @Autowired DatabaseManager databaseManager;

  @Transactional
  public SiteEntity findByName(String siteName) {
    log.debug("Search site with name " + siteName);
    return databaseManager.getJDBCTemplate().query("SELECT id, name FROM sites WHERE name = ?", new SiteRowMapper(), siteName).stream().findFirst().orElse(null);
  }

  public SiteEntity get(int id) {
    log.debug("Search site with id " + id);
    return databaseManager.getJDBCTemplate().query("SELECT id, name FROM sites WHERE id = ?", new SiteRowMapper(), id).stream().findFirst().orElse(null);
  }
}
