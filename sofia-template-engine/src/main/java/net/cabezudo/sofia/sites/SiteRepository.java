package net.cabezudo.sofia.sites;

import net.cabezudo.sofia.core.SofiaRuntimeException;
import net.cabezudo.sofia.persistence.DatabaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class SiteRepository {
  private static final Logger log = LoggerFactory.getLogger(SiteRepository.class);
  private @Autowired DatabaseManager databaseManager;

  @Transactional
  public SiteEntity findByName(String siteName) {
    log.debug("Search site with name " + siteName);

    List<SiteEntity> list = databaseManager.getJDBCTemplate().query("SELECT id, name FROM sites WHERE name = ?", new SiteRowMapper(), siteName);

    if (list.size() == 0) {
      log.warn("The host " + siteName + " is not found in the database.");
      return null;
    }
    if (list.size() == 1) {
      return list.get(0);
    }
    throw new SofiaRuntimeException("More than one register in query");
  }

  public SiteEntity get(int id) {
    log.debug("Search site with id " + id);

    List<SiteEntity> list = databaseManager.getJDBCTemplate().query("SELECT id, name FROM sites WHERE id = ?", new SiteRowMapper(), id);

    if (list.size() == 0) {
      log.warn("The site with id " + id + " is not found in the database.");
      return null;
    }
    if (list.size() == 1) {
      return list.get(0);
    }
    throw new SofiaRuntimeException("More than one register in query");
  }
}
