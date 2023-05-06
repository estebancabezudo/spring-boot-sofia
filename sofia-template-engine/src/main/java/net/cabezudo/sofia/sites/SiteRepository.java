package net.cabezudo.sofia.sites;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class SiteRepository {
  private static final Logger log = LoggerFactory.getLogger(SiteRepository.class);
  private @Autowired JdbcTemplate jdbcTemplate;

  @Transactional
  public SiteEntity findByName(String siteName) {
    log.debug("Search site with name " + siteName);

    return jdbcTemplate.queryForObject("SELECT id, name FROM sites WHERE name = ?", new SiteRowMapper(), siteName);
  }
}
