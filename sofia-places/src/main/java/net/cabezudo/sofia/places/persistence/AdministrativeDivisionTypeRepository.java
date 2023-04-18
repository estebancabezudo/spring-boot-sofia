package net.cabezudo.sofia.places.persistence;

import net.cabezudo.sofia.core.SofiaRuntimeException;
import net.cabezudo.sofia.places.mappers.AdministrativeDivisionTypeRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;

@Component
public class AdministrativeDivisionTypeRepository {
  Logger log = LoggerFactory.getLogger(AdministrativeDivisionTypeRepository.class);
  @Autowired
  private JdbcTemplate jdbcTemplate;

  public AdministrativeDivisionTypeEntity findByName(String name) {
    log.debug("Search administrative division type with name " + name);

    List<AdministrativeDivisionTypeEntity> list = jdbcTemplate.query(
        "SELECT id, name FROM administrative_division_types AS a " +
            "WHERE name = ?",
        new AdministrativeDivisionTypeRowMapper(), name);

    if (list.size() == 0) {
      return null;
    }
    if (list.size() == 1) {
      return list.get(0);
    }
    throw new SofiaRuntimeException("More than one register in query");
  }

  public AdministrativeDivisionTypeEntity create(String name) {
    String sqlQuery =
        "INSERT INTO administrative_division_types (name) VALUES (?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(sqlQuery, new String[]{"id"});
      ps.setString(1, name);
      return ps;
    }, keyHolder);
    int id = keyHolder.getKey().intValue();
    return new AdministrativeDivisionTypeEntity(id, name);
  }
}
