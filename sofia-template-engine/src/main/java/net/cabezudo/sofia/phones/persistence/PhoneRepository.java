package net.cabezudo.sofia.phones.persistence;

import net.cabezudo.sofia.persistence.DatabaseManager;
import net.cabezudo.sofia.persistence.NullKeyException;
import net.cabezudo.sofia.phones.mappers.PhoneRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;

@Repository
public class PhoneRepository {
  private static final Logger log = LoggerFactory.getLogger(PhoneRepository.class);
  private @Autowired DatabaseManager databaseManager;

  public PhoneEntity create(JdbcTemplate jdbcTemplate, int countryCode, Long number) {
    String sqlQuery = "INSERT INTO phones (country_code, number) VALUES (?, ?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(sqlQuery, new String[]{"id"});
      ps.setInt(1, countryCode);
      ps.setLong(2, number);
      return ps;
    }, keyHolder);
    Number key = keyHolder.getKey();
    if (key == null) {
      throw new NullKeyException();
    }
    int id = key.intValue();
    return new PhoneEntity(id, countryCode, number);
  }

  public PhoneEntity findOrCreate(JdbcTemplate jdbcTemplate, Integer countryCode, Long number) {
    PhoneEntity personalPhoneEntity = this.get(jdbcTemplate, countryCode, number);
    if (personalPhoneEntity == null) {
      return this.create(jdbcTemplate, countryCode, number);
    }
    return personalPhoneEntity;
  }

  public PhoneEntity get(Integer id) {
    JdbcTemplate jdbcTemplate = databaseManager.getJDBCTemplate();
    return get(jdbcTemplate, id);
  }

  public PhoneEntity get(JdbcTemplate jdbcTemplate, Integer id) {
    log.debug("Search phone by id " + id);
    if (id == null) {
      return null;
    }
    String query = "SELECT id, country_code, number FROM phones WHERE id = ?";
    return jdbcTemplate.query(query, new PhoneRowMapper(), id).stream().findFirst().orElse(null);
  }

  public PhoneEntity get(JdbcTemplate jdbcTemplate, Integer countryCode, Long number) {
    log.debug("Search phone by country code and number " + countryCode + " " + number);
    String query = "SELECT id, country_code, number FROM phones WHERE country_code = ? AND number = ?";
    return jdbcTemplate.query(query, new PhoneRowMapper(), countryCode, number).stream().findFirst().orElse(null);
  }
}
