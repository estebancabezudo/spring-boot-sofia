package net.cabezudo.sofia.places.persistence;

import net.cabezudo.sofia.core.SofiaRuntimeException;
import net.cabezudo.sofia.persistence.DatabaseManager;
import net.cabezudo.sofia.persistence.NullKeyException;
import net.cabezudo.sofia.places.Street;
import net.cabezudo.sofia.places.mappers.StreetRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class StreetRepository {
  private static final Logger log = LoggerFactory.getLogger(StreetRepository.class);
  private @Autowired DatabaseManager databaseManager;

  public StreetEntity getByNameOrCreate(JdbcTemplate jdbcTemplate, Street s) {
    if (s == null || s.getName() == null || s.getName().isBlank()) {
      return null;
    }
    StreetEntity street = get(s.getName());
    if (street == null) {
      return create(jdbcTemplate, s.getName(), s.isVerified());
    }
    return street;
  }

  public StreetEntity get(JdbcTemplate jdbcTemplate, Integer id) {
    log.debug("Search street by id: " + id);

    List<StreetEntity> list = jdbcTemplate.query(
        "SELECT id, name, verified FROM streets AS a WHERE id = ?",
        new StreetRowMapper(), id);

    if (list.size() == 0) {
      return null;
    }
    if (list.size() == 1) {
      return list.get(0);
    }
    throw new SofiaRuntimeException("More than one register in query");
  }

  public StreetEntity get(String name) {
    log.debug("Search street by name: " + name);

    if (name == null) {
      return null;
    }

    List<StreetEntity> list = databaseManager.getJDBCTemplate().query(
        "SELECT id, name, verified FROM streets AS a WHERE name = ?",
        new StreetRowMapper(), name);

    if (list.size() == 0) {
      return null;
    }
    if (list.size() == 1) {
      return list.get(0);
    }
    throw new SofiaRuntimeException("More than one register in query");
  }

  public StreetEntity create(JdbcTemplate jdbcTemplate, String name, boolean verified) {
    String sqlQuery = "INSERT INTO streets (name, verified) VALUES (?, ?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(sqlQuery, new String[]{"id"});
      ps.setString(1, name);
      ps.setBoolean(2, verified);
      return ps;
    }, keyHolder);
    Number key = keyHolder.getKey();
    if (key == null) {
      throw new NullKeyException();
    }
    int id = key.intValue();
    return new StreetEntity(id, name, verified);
  }

  public JdbcTemplate getJDBCTemplate() {
    return databaseManager.getJDBCTemplate();
  }
}
