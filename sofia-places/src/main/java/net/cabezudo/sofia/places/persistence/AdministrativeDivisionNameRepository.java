package net.cabezudo.sofia.places.persistence;

import net.cabezudo.sofia.core.SofiaRuntimeException;
import net.cabezudo.sofia.persistence.DatabaseManager;
import net.cabezudo.sofia.places.mappers.AdministrativeDivisionNameRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class AdministrativeDivisionNameRepository {
  private static final Logger log = LoggerFactory.getLogger(AdministrativeDivisionNameRepository.class);
  private @Autowired DatabaseManager databaseManager;

  public AdministrativeDivisionNameEntity findByName(String name) {
    log.debug("Search administrative division name " + name);

    List<AdministrativeDivisionNameEntity> list = databaseManager.getJDBCTemplate().query(
        "SELECT id, name " +
            "FROM administrative_division_names AS a " +
            "WHERE name = ?",
        new AdministrativeDivisionNameRowMapper(), name);
    if (list.size() == 0) {
      return null;
    }
    if (list.size() == 1) {
      return list.get(0);
    }
    throw new SofiaRuntimeException("More than one register in query");
  }

  public AdministrativeDivisionNameEntity create(String name) {
    String sqlQuery = "INSERT INTO administrative_division_names (name) VALUES (?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();
    databaseManager.getJDBCTemplate().update(connection -> {
      PreparedStatement ps = connection.prepareStatement(sqlQuery, new String[]{"id"});
      ps.setString(1, name);
      return ps;
    }, keyHolder);
    // FIX Method invocation 'intValue' may produce 'NullPointerException'
    int id = keyHolder.getKey().intValue();
    return new AdministrativeDivisionNameEntity(id, name);
  }

  public AdministrativeDivisionNameEntity findByNameOrCreate(String name) {
    if (name == null) {
      return null;
    }
    AdministrativeDivisionNameEntity administrativeDivisionNameEntity = this.findByName(name);

    if (administrativeDivisionNameEntity == null) {
      return this.create(name);
    }
    return administrativeDivisionNameEntity;
  }
}
