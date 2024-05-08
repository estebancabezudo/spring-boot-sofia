package net.cabezudo.sofia.language.persistence;

import net.cabezudo.sofia.language.LanguageRowMapper;
import net.cabezudo.sofia.persistence.DatabaseManager;
import net.cabezudo.sofia.persistence.NullKeyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;

@Repository
public class LanguageRepository {
  private static final Logger log = LoggerFactory.getLogger(LanguageRepository.class);
  private @Autowired DatabaseManager databaseManager;

  public LanguageEntity getByCode(String name) {
    JdbcTemplate jdbcTemplate = databaseManager.getJDBCTemplate();
    return getByCode(jdbcTemplate, name);
  }

  public LanguageEntity getByCode(JdbcTemplate jdbcTemplate, String code) {
    log.debug("Search language by code " + code);
    String query = "SELECT id, code FROM languages WHERE code = ?";
    return jdbcTemplate.query(query, new LanguageRowMapper(), code).stream().findFirst().orElse(null);
  }

  public LanguageEntity create(String code) {
    JdbcTemplate jdbcTemplate = databaseManager.getJDBCTemplate();
    return create(jdbcTemplate, code);
  }

  public LanguageEntity create(JdbcTemplate jdbcTemplate, String code) {
    String sqlQuery = "INSERT INTO languages (code) VALUES (?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(sqlQuery, new String[]{"id"});
      ps.setString(1, code);
      return ps;
    }, keyHolder);
    Number key = keyHolder.getKey();
    if (key == null) {
      throw new NullKeyException();
    }
    int id = key.intValue();
    return new LanguageEntity(id, code);
  }

  public LanguageEntity findOrCreate(JdbcTemplate jdbcTemplate, String code) {
    LanguageEntity languageEntity = this.getByCode(code);
    if (languageEntity == null) {
      return this.create(jdbcTemplate, code);
    }
    return languageEntity;
  }
}
