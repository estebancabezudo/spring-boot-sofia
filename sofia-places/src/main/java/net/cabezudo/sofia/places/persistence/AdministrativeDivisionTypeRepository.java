package net.cabezudo.sofia.places.persistence;

import net.cabezudo.sofia.core.SofiaRuntimeException;
import net.cabezudo.sofia.persistence.DatabaseManager;
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
  private static final Logger log = LoggerFactory.getLogger(AdministrativeDivisionTypeRepository.class);
  private @Autowired DatabaseManager databaseManager;

  public AdministrativeDivisionTypeEntity findByName(String name) {
    return findByName(databaseManager.getJDBCTemplate(), name);
  }

  public AdministrativeDivisionTypeEntity findByName(JdbcTemplate jdbcTemplate, String name) {
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
    return create(databaseManager.getJDBCTemplate(), name);
  }

  public AdministrativeDivisionTypeEntity create(JdbcTemplate jdbcTemplate, String name) {
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

  public AdministrativeDivisionTypeEntity findOrCreate(JdbcTemplate jdbcTemplate, String name) {
    AdministrativeDivisionTypeEntity administrativeDivisionTypeEntity = this.findByName(jdbcTemplate, name);
    if (administrativeDivisionTypeEntity == null) {
      return this.create(jdbcTemplate, name);
    }
    return administrativeDivisionTypeEntity;
  }

  public AdministrativeDivisionTypeEntity findByNameOrCreate(String name) {
    AdministrativeDivisionTypeEntity administrativeDivisionTypeEntity = this.findByName(name);
    if (administrativeDivisionTypeEntity == null) {
      return this.create(name);
    }
    return administrativeDivisionTypeEntity;
  }
}


